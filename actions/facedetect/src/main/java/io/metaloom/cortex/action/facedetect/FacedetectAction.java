package io.metaloom.cortex.action.facedetect;

import static io.metaloom.cortex.action.facedetect.FacedetectMedia.FACE_DETECTION;
import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.facedetect.file.model.FaceData;
import io.metaloom.cortex.action.facedetect.video.VideoFaceScanner;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.flag.FaceDetectionFlag;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.cortex.common.dlib.DLibModelProvisioner;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.cortex.action.facedetect.avro.Facedetection;
import io.metaloom.loom.rest.model.asset.AssetResponse;
import io.metaloom.utils.hash.SHA512;
import io.metaloom.video.facedetect.dlib.impl.DLibFacedetector;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;;

@Singleton
public class FacedetectAction extends AbstractMediaAction<FacedetectActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(FacedetectAction.class);

	private static final int WINDOW_COUNT = 10;
	private static final int WINDOW_SIZE = 10;
	private static final int WINDOW_STEPS = 5;

	protected VideoFaceScanner videoDetector;
	protected DLibFacedetector detector;

	@Inject
	public FacedetectAction(@Nullable LoomClient client, CortexOptions cortexOption, FacedetectActionOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public void initialize() {
		Video4j.init();
		videoDetector = new VideoFaceScanner();
		try {
			DLibModelProvisioner.extractModelData(Paths.get("dlib"));
		} catch (IOException e) {
			throw new RuntimeException("Failed to extract dlib models", e);
		}
		try {
			this.detector = DLibFacedetector.create();
			this.detector.setMinFaceHeightFactor(options().getMinFaceHeightFactor());
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
	protected boolean isProcessable(ActionContext ctx) {
		LoomMedia media = ctx.media();
		return media.isVideo() || media.isImage();
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		FacedetectMedia media = ctx.media(FACE_DETECTION);
		// TODO check the flags with the options to figure out whether we need to rerun the detection
		return media.hasFacedetectionFlag();
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws IOException {
		LoomMedia media = ctx.media();
		if (media.isVideo()) {
			return processVideo(ctx);
		} else if (media.isImage()) {
			return processImage(ctx);
		} else {
			return ctx.skipped("No visual media").next();
		}
	}

	private ActionResult processImage(ActionContext ctx) throws IOException {

		FacedetectMedia media = ctx.media(FACE_DETECTION);
		SHA512 hash = media.getSHA512();
		// 1. Read image
		BufferedImage image = ImageIO.read(media.file());
		List<? extends Face> result = detector.detectFaces(image);

		if (result != null && !result.isEmpty()) {
			media.setFaceCount(result.size());
			media.setFacedetectionFlag(FaceDetectionFlag.SUCCESS);
			Facedetection detection = new Facedetection();
//			FaceData params = new FaceData(hash);
//			params.getEntries().addAll(result);
			// TODO add face data
			media.setFacedetectionParams(detection);
		}
		// TODO handle faces / get embeddings
		return ctx.origin(COMPUTED).next();
	}

	private ActionResult processVideo(ActionContext ctx) {
		FacedetectMedia media = ctx.media(FACE_DETECTION);
		try (VideoFile video = Videos.open(media.absolutePath())) {
			List<Face> faces = videoDetector.scan(video, WINDOW_COUNT, WINDOW_SIZE, WINDOW_STEPS);
			media.setFaceCount(faces.size());
			// TODO add params, flags
			return ctx.origin(COMPUTED).next();
		} catch (InterruptedException e) {
			log.error("Failed to process video", e);
			return ctx.failure(e.getMessage()).next();
		}

	}

}
