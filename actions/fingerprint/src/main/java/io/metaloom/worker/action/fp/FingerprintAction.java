package io.metaloom.worker.action.fp;

import static io.metaloom.worker.action.api.ProcessableMediaMeta.FINGERPRINT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.video4j.Video;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.fingerprint.Fingerprint;
import io.metaloom.video4j.fingerprint.v2.MultiSectorVideoFingerprinter;
import io.metaloom.video4j.fingerprint.v2.impl.MultiSectorVideoFingerprinterImpl;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;
import io.metaloom.worker.action.common.AbstractFilesystemAction;
import io.metaloom.worker.action.common.settings.ProcessorSettings;

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
	public ActionResult process(ProcessableMedia media) {
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
		String sha512 = media.getHash512();
		try {
			processMedia(sha512, media);
			return ActionResult.processed(true, start);
		} catch (Exception e) {
			error(media, "Failure for " + media.path());
			if (log.isErrorEnabled()) {
				log.error("Error while processing media " + media.path(), e);
			}
			if (getFingerprint(media) == null) {
				writeFingerprint(media, "NULL");
			}
			return ActionResult.failed(true, start);
		}

	}

	private void processMedia(String sha512, ProcessableMedia media) throws InterruptedException {
		long start = System.currentTimeMillis();
		String fp = getFingerprint(media);
		boolean isNull = fp != null && fp.equals("NULL");
		boolean isCorrect = fp != null && fp.length() == 66;
		if (!settings().isRetryFailed() && (isNull || isCorrect)) {
			print(media, "DONE", "", start);
		} else {
			AssetResponse entry = client().loadAsset(sha512).sync();
			// Sync from db
			if (entry != null) {
				String dbFP = entry.getFingerprint();
				if (dbFP != null) {
					writeFingerprint(media, dbFP);
					print(media, "DONE", "(from db)", start);
					return;
				}
			}
			String path = media.absolutePath();
			try (Video video = Videos.open(path)) {
				Fingerprint fingerprint = hasher.hash(video);
				if (fingerprint == null) {
					print(media, "NULL", "(no result)", start);
					writeFingerprint(media, "NULL");
				} else {
					String hash = fingerprint.hex();
					print(media, "DONE", "", start);
					writeFingerprint(media, hash);
				}
			}
		}
	}

	private String getFingerprint(ProcessableMedia media) {
		return media.get(FINGERPRINT);
	}

	private void writeFingerprint(ProcessableMedia media, String fp) {
		media.put(FINGERPRINT, fp);
	}

}
