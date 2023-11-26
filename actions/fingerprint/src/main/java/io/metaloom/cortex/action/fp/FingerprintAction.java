package io.metaloom.cortex.action.fp;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.metaloom.cortex.api.action.ActionResult2.CONTINUE_NEXT;
import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.context.ActionContext;
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
	public ActionResult2 process(ActionContext ctx) {
		LoomMedia media = ctx.media();
		if (!media.isVideo()) {
			print(ctx, "SKIPPED", "(no video)");
			return ctx.skipped().next();
		}
		if (!options().isProcessIncomplete()) {
			Boolean isComplete = media.isComplete();
			if (isComplete != null && !isComplete) {
				print(ctx, "SKIPPED", "(is incomplete)");
				return ctx.skipped().next();
			}
		}
		String fingerprint = media.getFingerprint();
		String info = "";
		if (fingerprint == null) {
			SHA512 sha512 = media.getSHA512();
			try {
				processMedia(sha512, ctx);
				return ctx.origin(COMPUTED).next();
			} catch (Exception e) {
				error(media, "Failure for " + media.path());
				if (log.isErrorEnabled()) {
					log.error("Error while processing media " + media.path(), e);
				}
				if (media.getFingerprint() == null) {
					media.setFingerprint("NULL");
				}
				return ctx.failure("").next();
			}
		} else {
			return ctx.next();
		}

	}

	private void processMedia(SHA512 sha512, ActionContext ctx) throws InterruptedException {
		LoomMedia media = ctx.media();
		String fp = media.getFingerprint();
		boolean isNull = fp != null && fp.equals("NULL");
		boolean isCorrect = fp != null && fp.length() == 66;
		if (!options().isRetryFailed() && (isNull || isCorrect)) {
			print(ctx, "DONE", "");
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
					print(ctx, "DONE", "(from db)");
					return;
				}
			}
			String path = media.absolutePath();
			try (VideoFile video = Videos.open(path)) {
				Fingerprint fingerprint = hasher.hash(video);
				if (fingerprint == null) {
					print(ctx, "NULL", "(no result)");
					media.setFingerprint("NULL");
				} else {
					String hash = fingerprint.hex();
					print(ctx, "DONE", "");
					media.setFingerprint(hash);
				}
			}
		}
	}

}
