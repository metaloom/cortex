package io.metaloom.cortex.action.thumbnail;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.preview.PreviewGenerator;

@Singleton
public class ThumbnailAction extends AbstractFilesystemAction<ThumbnailActionOptions> {

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
	public ActionResult2 process(ActionContext ctx) {
		if (options().getThumbnailPath() == null) {
			// throw new RuntimeException("No thumbnail output directory has been configured");
			return ctx.skipped().next();
		}
		LoomMedia media = ctx.media();
		if (!media.isVideo()) {
			ctx.print( "SKIPPED", "(no video)");
			return ctx.skipped().next();
		}
		if (!new File(options().getThumbnailPath()).exists()) {
			ctx.print( "SKIPPED", "(thumbnail dir not found)");
			return ctx.skipped().next();
		}

		if (!options().isProcessIncomplete()) {
			Boolean isComplete = media.isComplete();
			if (isComplete != null && !isComplete) {
				ctx.print( "SKIPPED", "(is incomplete)");
				return ctx.skipped().next();
			}
		}

		return processMedia(ctx);
	}

	private ActionResult2 processMedia(ActionContext ctx) {
		LoomMedia media = ctx.media();
		File outputFile = new File(options().getThumbnailPath(), media.getSHA512() + ".jpg");
		String flags = media.getThumbnailFlags();
		boolean isNull = flags != null && flags.equals(NULL_FLAG);
		boolean isDone = flags != null && flags.equals(DONE_FLAG);

		if (isDone) {
			ctx.print( "DONE", "");
			return ctx.next();
		}

		if (hasThumbnail(media)) {
			if (!isDone) {
				media.setThumbnailFlags("DONE");
			}
			ctx.print("DONE", "");
			return ctx.next();
		}

		if (!options().isRetryFailed() && isNull) {
			ctx.print("FAILED", "(previously failed)");
			return ctx.failure("").next();
		}

		try {
			String path = media.absolutePath();
			try (VideoFile video = Videos.open(path)) {
				gen.save(video, outputFile);
				ctx.print("DONE", "");
				media.setThumbnailFlags(DONE_FLAG);
			}
		} catch (Exception e) {
			e.printStackTrace();
			media.setThumbnailFlags(NULL_FLAG);
			// TODO update failed status
			// touchFailed(media);
			error(media, "NULL");
		}
		
		return ctx.next();

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
