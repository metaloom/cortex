package io.metaloom.cortex.action.facedetect;

import static io.metaloom.cortex.api.action.ActionResult2.CONTINUE_NEXT;
import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.common.dlib.DLibModelProvisioner;
import io.metaloom.cortex.action.facedetect.video.VideoFaceScanner;
import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.action.media.flag.FaceDetectionFlags;
import io.metaloom.cortex.api.action.media.param.FaceDetectionParameters;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.video.facedetect.dlib.impl.DLibFacedetector;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;

@Singleton
public class FacedetectAction extends AbstractFilesystemAction<FacedetectActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(FacedetectAction.class);

	protected final DLibFacedetector detector;
	protected final VideoFaceScanner videoDetector = new VideoFaceScanner();

	private static final int WINDOW_COUNT = 10;
	private static final int WINDOW_SIZE = 10;
	private static final int WINDOW_STEPS = 5;

	static {
		Video4j.init();
	}

	@Inject
	public FacedetectAction(LoomGRPCClient client, CortexOptions cortexOption, FacedetectActionOptions options) {
		super(client, cortexOption, options);
		try {
			DLibModelProvisioner.extractModelData(Paths.get("dlib"));
		} catch (IOException e) {
			throw new RuntimeException("Failed to extract dlib models", e);
		}
		try {
			this.detector = DLibFacedetector.create();
			this.detector.setMinFaceHeightFactor(options.getMinFaceHeightFactor());
		} catch (FileNotFoundException e) {
			log.error("Failed to load dlib", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public String name() {
		return "facedetect";
	}

	@Override
	public ActionResult2 process(ActionContext ctx) {
		LoomMedia media = ctx.media();
		try {
			if (media.isVideo()) {
				return processVideo(ctx);
			} else if (media.isImage()) {
				return processImage(ctx);
			} else {
				// is unprocessable media type
				return ctx.skipped().next();
			}
		} catch (Exception e) {
			log.error("Failed to process media", e);
			return ctx.failure("").next();
		}
	}

	private ActionResult2 processImage(ActionContext ctx) throws IOException {

		LoomMedia media = ctx.media();

		// 1. Read image
		BufferedImage image = ImageIO.read(media.file());
		List<? extends Face> result = detector.detectFaces(image);

		if (result != null && !result.isEmpty()) {
			media.setFaceCount(result.size());
			media.setFacedetectionFlags(FaceDetectionFlags.SUCCESS);
			FaceDetectionParameters params = new FaceDetectionParameters();
			params.setCount(result.size());
			// TODO add face data
			media.setFacedetectionParams(params);
		}
		// TODO handle faces / get embeddings
		return ctx.origin(COMPUTED).next();
	}

	private ActionResult2 processVideo(ActionContext ctx) {
		LoomMedia media = ctx.media();
		try (VideoFile video = Videos.open(media.absolutePath())) {
			List<Face> faces = videoDetector.scan(video, WINDOW_COUNT, WINDOW_SIZE, WINDOW_STEPS);
			media.setFaceCount(faces.size());
			// TODO add params, flags
		} catch (InterruptedException e) {
			log.error("Failed to process video", e);
			return ctx.failure("").next();
		}

		return ctx.origin(COMPUTED).next();
	}

}
