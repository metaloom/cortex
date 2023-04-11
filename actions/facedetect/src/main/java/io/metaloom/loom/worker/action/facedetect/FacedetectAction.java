package io.metaloom.loom.worker.action.facedetect;

import static io.metaloom.worker.action.api.ActionResult.CONTINUE_NEXT;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.worker.action.facedetect.cluster.ClusterResult;
import io.metaloom.loom.worker.action.facedetect.cluster.FaceClusterer;
import io.metaloom.video.facedetect.Face;
import io.metaloom.video.facedetect.FaceVideoFrame;
import io.metaloom.video.facedetect.dlib.impl.DLibFacedetector;
import io.metaloom.video4j.Video;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.opencv.CVUtils;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;
import io.metaloom.worker.action.api.ProcessableMediaMeta;
import io.metaloom.worker.action.common.AbstractFilesystemAction;
import io.metaloom.worker.action.common.dlib.DLibModelProvisioner;
import io.metaloom.worker.action.common.settings.ProcessorSettings;

public class FacedetectAction extends AbstractFilesystemAction<FacedetectActionSettings> {

	public static final Logger log = LoggerFactory.getLogger(FacedetectAction.class);

	private static final String NAME = "facedetect";
	protected final DLibFacedetector detector;

	static {
		Video4j.init();
	}

	public FacedetectAction(LoomGRPCClient client, ProcessorSettings processorSettings, FacedetectActionSettings settings)
		throws FileNotFoundException {
		super(client, processorSettings, settings);
		try {

			DLibModelProvisioner.extractModelData(Paths.get("dlib"));
		} catch (IOException e) {
			throw new RuntimeException("Failed to extract dlib models", e);
		}
		this.detector = DLibFacedetector.create();
		this.detector.setMinFaceHeightFactor(settings.getMinFaceHeightFactor());

	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(ProcessableMedia media) {
		long start = System.currentTimeMillis();
		try {
			if (media.isVideo()) {
				return processVideo(media);
			} else if (media.isImage()) {
				return processImage(media);
			} else {
				return skipped(media, start, "is unprocessable media type");
			}
		} catch (Exception e) {
			log.error("Failed to process media", e);
			return ActionResult.failed(CONTINUE_NEXT, start);
		}
	}

	private ActionResult processImage(ProcessableMedia media) throws IOException {
		long start = System.currentTimeMillis();
		// 1. Read image
		BufferedImage image = ImageIO.read(media.file());
		List<? extends Face> result = detector.detect(image);

		if (result != null && !result.isEmpty()) {
			media.put(ProcessableMediaMeta.FACES, result);
		}
		// TODO handle faces / get embeddings
		return done(media, start, "image processed");
	}

	private ActionResult processVideo(ProcessableMedia media) {
		long start = System.currentTimeMillis();
		List<Face> faces = new ArrayList<>();
		try (Video video = Videos.open(media.absolutePath())) {
			// FacedetectorMetrics metrics = FacedetectorMetrics.create();
			Stream<FaceVideoFrame> frameStream = video.streamFrames()
				.filter(frame -> {
					return frame.number() % settings().getVideoChopRate() == 0;
				})
				.map(frame -> {
					CVUtils.boxFrame2(frame, settings().getVideoScaleSize());
					return frame;
				})
				.map(detector::detect)
				.filter(FaceVideoFrame::hasFace);
			// .map(metrics::track)
			// .map(detector::markFaces)
			// .map(detector::markLandmarks);
			// VideoUtils.showVideoFrameStream(frameStream);

			frameStream.forEach(frame -> {
				for (Face face : frame.faces()) {
					if (face.getEmbeddings() != null) {
						faces.add(face);
					}
				}
			});
		}
		ClusterResult clusterResult = clusterFaces(faces);
		media.put(ProcessableMediaMeta.FACE_CLUSTERS, clusterResult);
		media.put(ProcessableMediaMeta.FACES, faces);
		return done(media, start, "facedetection completed");
	}

	private ClusterResult clusterFaces(List<Face> faces) {
		return FaceClusterer.clusterFaces(faces, settings().getFaceClusterEPS(), settings().getFaceClusterMinimum());
	}

}
