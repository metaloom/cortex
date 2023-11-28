package io.metaloom.cortex.action.thumbnail;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.preview.PreviewGenerator;

@Singleton
public class ThumbnailAction extends AbstractMediaAction<ThumbnailActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(ThumbnailAction.class);

	public static String DONE_FLAG = "DONE";

	public static String NULL_FLAG = "NULL";

	private final PreviewGenerator gen;

	static {
		Video4j.init();
	}

	@Inject
	public ThumbnailAction(LoomGRPCClient client, CortexOptions options, ThumbnailActionOptions actionOptions) {
		super(client, options, actionOptions);
		int tileSize = actionOptions.getTileSize();
		int cols = actionOptions.getCols();
		int rows = actionOptions.getRows();
		this.gen = new PreviewGenerator(tileSize, cols, rows);
	}

	@Override
	public String name() {
		return "thumbnail";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		if (options().getThumbnailPath() == null) {
			// return ctx.skipped("thumbnail dir not configured").next();
			return false;
		}

		if (!new File(options().getThumbnailPath()).exists()) {
			// return ctx.skipped("thumbnail dir not found").next();
			return false;
		}

		LoomMedia media = ctx.media();
		if (!options().isProcessIncomplete()) {
			Boolean isComplete = media.isComplete();
			if (isComplete != null && !isComplete) {
				// return ctx.skipped("incomplete media").next();
				return false;
			}
		}

		return ctx.media().isVideo();
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		LoomMedia media = ctx.media();
		File outputFile = new File(options().getThumbnailPath(), media.getSHA512() + ".jpg");
		if (outputFile.exists()) {
			return true;
		}

		String flags = media.getThumbnailFlags();

		boolean isDone = flags != null && flags.equals(DONE_FLAG);

		if (isDone) {
			ctx.print("DONE", "");
			return true;
		}

		boolean isNull = flags != null && flags.equals(NULL_FLAG);
		if (hasThumbnail(media)) {
			// if (!isDone) {
			media.setThumbnailFlags("DONE");
			// }
			// ctx.print("DONE", "");
			return true;
		}

		if (options().isRetryFailed() && isNull) {
			// ctx.print("FAILED", "(previously failed)");
			return false;
		}

		return true;
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws IOException {
		LoomMedia media = ctx.media();
		File outputFile = new File(options().getThumbnailPath(), media.getSHA512() + ".jpg");

		try {
			String path = media.absolutePath();
			try (VideoFile video = Videos.open(path)) {
				gen.save(video, outputFile);
				ctx.print("DONE", "");
				media.setThumbnailFlags(DONE_FLAG);
			}
			return ctx.origin(COMPUTED).next();
		} catch (Exception e) {
			e.printStackTrace();
			media.setThumbnailFlags(NULL_FLAG);
			// TODO update failed status
			// touchFailed(media);
			error(media, "NULL");
			return ctx.failure(e.getMessage()).next();
		}

	}

	/**
	 * Check whether the thumbnail already exists.
	 * 
	 * @param media
	 * @return
	 */
	private boolean hasThumbnail(LoomMedia media) {
		File outputFile = new File(options().getThumbnailPath(), media.getSHA512() + ".jpg");
		return outputFile.exists();
	}

}
