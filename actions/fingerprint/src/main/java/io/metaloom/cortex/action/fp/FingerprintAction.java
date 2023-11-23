package io.metaloom.cortex.action.fp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.media.LoomMedia;
import io.metaloom.cortex.action.common.AbstractFilesystemAction;
import io.metaloom.cortex.action.common.settings.ProcessorSettings;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.fingerprint.Fingerprint;
import io.metaloom.video4j.fingerprint.v2.MultiSectorVideoFingerprinter;
import io.metaloom.video4j.fingerprint.v2.impl.MultiSectorVideoFingerprinterImpl;

public class FingerprintAction extends AbstractFilesystemAction<FingerprintActionSettings> {

	public static final Logger log = LoggerFactory.getLogger(FingerprintAction.class);

	public static final String NAME = "fingerprint";

	private MultiSectorVideoFingerprinter hasher = new MultiSectorVideoFingerprinterImpl();

	static {
		Video4j.init();
	}

	public FingerprintAction(LoomGRPCClient client, ProcessorSettings processorSettings, FingerprintActionSettings settings) {
		super(client, processorSettings, settings);
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
		if (!settings().isProcessIncomplete()) {
			Boolean isComplete = media.isComplete();
			if (isComplete != null && !isComplete) {
				print(media, "SKIPPED", "(is incomplete)", start);
				return ActionResult.skipped(true, start);
			}
		}
		String fingerprint = media.getFingerprint();
		String info = "";
		if (fingerprint == null) {
			String sha512 = media.getSHA512();
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
			return done(media, start, info);
		}

	}

	private void processMedia(String sha512, LoomMedia media) throws InterruptedException {
		long start = System.currentTimeMillis();
		String fp = media.getFingerprint();
		boolean isNull = fp != null && fp.equals("NULL");
		boolean isCorrect = fp != null && fp.length() == 66;
		if (!settings().isRetryFailed() && (isNull || isCorrect)) {
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
