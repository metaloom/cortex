package io.metaloom.cortex.action.fp;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;
import static io.metaloom.cortex.api.action.ResultOrigin.REMOTE;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.fingerprint.Fingerprint;
import io.metaloom.video4j.fingerprint.v2.MultiSectorVideoFingerprinter;
import io.metaloom.video4j.fingerprint.v2.impl.MultiSectorVideoFingerprinterImpl;

@Singleton
public class FingerprintAction extends AbstractMediaAction<FingerprintOptions> {

	public static final Logger log = LoggerFactory.getLogger(FingerprintAction.class);

	private MultiSectorVideoFingerprinter hasher = new MultiSectorVideoFingerprinterImpl();

	static {
		Video4j.init();
	}

	@Inject
	public FingerprintAction(LoomGRPCClient client, CortexOptions cortexOption, FingerprintOptions options, MetaStorage storage) {
		super(client, cortexOption, options, storage);
	}

	@Override
	public String name() {
		return "fingerprint";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		// return ctx.skipped("no video media").next();
		LoomMedia media = ctx.media();
		// return ctx.skipped("incomplete media").next();
		boolean isComplete = media.isComplete() != null && media.isComplete();
		boolean isVideo = media.isVideo();
		if (isVideo && !isComplete && options().isProcessIncomplete()) {
			return true;
		} else {
			return isVideo;
		}
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		return ctx.media().getFingerprint() != null;
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) {
		LoomMedia media = ctx.media();
		if (asset != null && asset.getFingerprint() != null) {
			media.setFingerprint(asset.getFingerprint());
			return ctx.origin(REMOTE).next();
		} else {
			try {
				processMedia(ctx);
				return ctx.origin(COMPUTED).next();
			} catch (Exception e) {
				error(media, "Failure for " + media.path());
				if (log.isErrorEnabled()) {
					log.error("Error while processing media " + media.path(), e);
				}
				// Store failure flag
				if (media.getFingerprint() == null) {
					media.setFingerprint("NULL");
				}
				// TODO encode message
				return ctx.failure(e.getMessage()).next();
			}

		}
	}

	private void processMedia(ActionContext ctx) throws InterruptedException {
		LoomMedia media = ctx.media();
		String fp = media.getFingerprint();
		boolean isNull = fp != null && fp.equals("NULL");
		boolean isCorrect = fp != null && fp.length() == 66;
		if (!options().isRetryFailed() && (isNull || isCorrect)) {
			print(ctx, "DONE", "");
		} else {

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
