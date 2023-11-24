package io.metaloom.loom.cortex.action.thumbnail;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.utils.hash.SHA512;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.preview.PreviewGenerator;

@Singleton
public class ThumbnailAction extends AbstractFilesystemAction<ThumbnailOptions> {

	public static final Logger log = LoggerFactory.getLogger(ThumbnailAction.class);

	private static final String NAME = "thumbnail";

	public static String DONE_FLAG = "DONE";

	public static String NULL_FLAG = "NULL";

	private final PreviewGenerator gen;

	static {
		Video4j.init();
	}

	@Inject
	public ThumbnailAction(LoomGRPCClient client, CortexOptions options, ThumbnailOptions actionOptions) {
		super(client, options, actionOptions);
		int tileSize = actionOptions.getTileSize();
		int cols = actionOptions.getCols();
		int rows = actionOptions.getRows();
		this.gen = new PreviewGenerator(tileSize, cols, rows);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(LoomMedia media) {
		if (option().getThumbnailPath() == null) {
			throw new RuntimeException("No thumbnail output directory has been configured");
		}
		long start = System.currentTimeMillis();
		if (!media.isVideo()) {
			print(media, "SKIPPED", "(no video)", start);
			return ActionResult.skipped(true, start);
		}
		if (!new File(option().getThumbnailPath()).exists()) {
			print(media, "SKIPPED", "(thumbnail dir not found)", start);
			return ActionResult.skipped(true, start);
		}

		if (!option().isProcessIncomplete()) {
			Boolean isComplete = media.isComplete();
			if (isComplete != null && !isComplete) {
				print(media, "SKIPPED", "(is incomplete)", start);
				return ActionResult.skipped(true, start);
			}
		}

		SHA512 sha512 = media.getSHA512();
		processMedia(sha512, media);
		return ActionResult.processed(true, start);
	}

	private void processMedia(SHA512 sha512, LoomMedia media) {
		long start = System.currentTimeMillis();
		File outputFile = new File(option().getThumbnailPath(), media.getSHA512() + ".jpg");
		String flags = media.getThumbnailFlags();
		boolean isNull = flags != null && flags.equals(NULL_FLAG);
		boolean isDone = flags != null && flags.equals(DONE_FLAG);

		if (isDone) {
			print(media, "DONE", "", start);
			return;
		}

		if (hasThumbnail(media)) {
			if (!isDone) {
				media.setThumbnailFlags("DONE");
			}
			print(media, "DONE", "", start);
			return;
		}

		if (!option().isRetryFailed() && isNull) {
			print(media, "FAILED", "(previously failed)", start);
			return;
		}

		try {
			String path = media.absolutePath();
			try (VideoFile video = Videos.open(path)) {
				gen.save(video, outputFile);
				print(media, "DONE", "", start);
				media.setThumbnailFlags(DONE_FLAG);
			}
		} catch (Exception e) {
			e.printStackTrace();
			media.setThumbnailFlags(NULL_FLAG);
			// TODO update failed status
			// touchFailed(media);
			error(media, "NULL");
		}

	}

	/**
	 * Check whether the thumbnail already exists.
	 * 
	 * @param media
	 * @return
	 */
	private boolean hasThumbnail(LoomMedia media) {
		File outputFile = new File(option().getThumbnailPath(), media.getSHA512() + ".jpg");
		return outputFile.exists();
	}

}
