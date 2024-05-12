package io.metaloom.cortex.action.thumbnail;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;
import static io.metaloom.cortex.api.media.param.ThumbnailFlag.DONE;
import static io.metaloom.cortex.api.media.param.ThumbnailFlag.FAILED;
import static io.metaloom.cortex.media.consistency.ConsistencyMedia.CONSISTENCY;
import static io.metaloom.cortex.media.thumbnail.ThumbnailMedia.THUMBNAIL;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.param.ThumbnailFlag;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.cortex.media.consistency.ConsistencyMedia;
import io.metaloom.cortex.media.thumbnail.ThumbnailMedia;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.preview.PreviewGenerator;

@Singleton
public class ThumbnailAction extends AbstractMediaAction<ThumbnailActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(ThumbnailAction.class);

	private final PreviewGenerator gen;

	@Inject
	public ThumbnailAction(@Nullable LoomGRPCClient client, CortexOptions options, ThumbnailActionOptions actionOptions) {
		super(client, options, actionOptions);
		int tileSize = actionOptions.getTileSize();
		int cols = actionOptions.getCols();
		int rows = actionOptions.getRows();
		this.gen = new PreviewGenerator(tileSize, cols, rows);
	}

	@Override
	public void initialize() {
		Video4j.init();
	}

	@Override
	public String name() {
		return "thumbnail";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		ConsistencyMedia media = ctx.media(CONSISTENCY);
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
		ThumbnailMedia media = ctx.media(THUMBNAIL);
		if (media.hasThumbnail()) {
			return true;
		}

		ThumbnailFlag flag = media.getThumbnailFlags();
		boolean isDone = flag != null && flag == DONE;
		if (isDone) {
			ctx.print("DONE", "");
			return true;
		}

		boolean isNull = flag != null && flag == FAILED;
		if (media.hasThumbnail()) {
			// if (!isDone) {
			media.setThumbnailFlag(DONE);
			// }
			// ctx.print("DONE", "");
			return true;
		}

		if (options().isRetryFailed() && isNull) {
			// ctx.print("FAILED", "(previously failed)");
			return false;
		}

		return false;
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws IOException {
		ThumbnailMedia media = ctx.media(THUMBNAIL);

		try {
			String path = media.absolutePath();
			try (VideoFile video = Videos.open(path)) {
				try (OutputStream os = media.storage().outputStream(media, ThumbnailMedia.THUMBNAIL_BIN_KEY)) {
					gen.save(video, os);
					ctx.print("DONE", "");
					media.setThumbnailFlag(DONE);
				}
			}
			return ctx.origin(COMPUTED).next();
		} catch (Exception e) {
			log.error("Failed to compute thumbnail", e);
			media.setThumbnailFlag(FAILED);
			// TODO update failed status
			// touchFailed(media);
			error(media, "NULL");
			return ctx.failure(e.getMessage()).next();
		}
	}
}
