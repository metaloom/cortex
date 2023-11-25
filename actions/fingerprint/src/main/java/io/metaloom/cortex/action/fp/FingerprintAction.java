package io.metaloom.cortex.action.fp;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.SHA512;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.fingerprint.Fingerprint;
import io.metaloom.video4j.fingerprint.v2.MultiSectorVideoFingerprinter;
import io.metaloom.video4j.fingerprint.v2.impl.MultiSectorVideoFingerprinterImpl;

@Singleton
public class FingerprintAction extends AbstractFilesystemAction<FingerprintOptions> {

	public static final Logger log = LoggerFactory.getLogger(FingerprintAction.class);

	public static final String NAME = "fingerprint";

	private MultiSectorVideoFingerprinter hasher = new MultiSectorVideoFingerprinterImpl();

	private FingerprintOptions foption;

	static {
		Video4j.init();
	}

	@Inject
	public FingerprintAction(LoomGRPCClient client, CortexOptions cortexOption, FingerprintOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(LoomMedia media) {
		long start = System.currentTimeMillis();
		if (!media.isVideo()) {
			print(media, "SKIPPED", "(no video)", start);
			return ActionResult.skipped(true, start);
		}
		if (!foption.isProcessIncomplete()) {
			Boolean isComplete = media.isComplete();
			if (isComplete != null && !isComplete) {
				print(media, "SKIPPED", "(is incomplete)", start);
				return ActionResult.skipped(true, start);
			}
		}
		String fingerprint = media.getFingerprint();
		String info = "";
		if (fingerprint == null) {
			SHA512 sha512 = media.getSHA512();
			try {
				info = "computed";
				processMedia(sha512, media);
				return ActionResult.processed(true, start);
			} catch (Exception e) {
				error(media, "Failure for " + media.path());
				if (log.isErrorEnabled()) {
					log.error("Error while processing media " + media.path(), e);
				}
				if (media.getFingerprint() == null) {
					media.setFingerprint("NULL");
				}
				return ActionResult.failed(true, start);
			}
		} else {
			return success(media, info);
		}

	}

	private void processMedia(SHA512 sha512, LoomMedia media) throws InterruptedException {
		long start = System.currentTimeMillis();
		String fp = media.getFingerprint();
		boolean isNull = fp != null && fp.equals("NULL");
		boolean isCorrect = fp != null && fp.length() == 66;
		if (!foption.isRetryFailed() && (isNull || isCorrect)) {
			print(media, "DONE", "", start);
		} else {
			AssetResponse asset = null;
			if (!isOfflineMode()) {
				asset = client().loadAsset(sha512).sync();
			}
			// Sync from db
			if (asset != null) {
				String dbFP = asset.getFingerprint();
				if (dbFP != null) {
					media.setFingerprint(dbFP);
					print(media, "DONE", "(from db)", start);
					return;
				}
			}
			String path = media.absolutePath();
			try (VideoFile video = Videos.open(path)) {
				Fingerprint fingerprint = hasher.hash(video);
				if (fingerprint == null) {
					print(media, "NULL", "(no result)", start);
					media.setFingerprint("NULL");
				} else {
					String hash = fingerprint.hex();
					print(media, "DONE", "", start);
					media.setFingerprint(hash);
				}
			}
		}
	}

}
