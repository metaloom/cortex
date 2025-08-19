package io.metaloom.loom.cortex.action.facedetect;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.metaloom.inspireface4j.BoundingBox;
import io.metaloom.inspireface4j.Detection;
import io.metaloom.inspireface4j.FaceAttributes;
import io.metaloom.inspireface4j.InspirefaceLib;
import io.metaloom.inspireface4j.InspirefaceSession;
import io.metaloom.inspireface4j.data.FaceDetections;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.VideoFrame;
import io.metaloom.video4j.opencv.CVUtils;
import io.metaloom.video4j.utils.SimpleImageViewer;

public class InspirefaceTest {

	private static final String DEFAULT_PACK = "/home/jotschi/workspaces/metaloom/inspireface4j/packs/Pikachu";

	@Test
	public void testVideoUsageExample() throws IOException {
		// SNIPPET START video-usage.example

		// Initialize video4j and InspirefaceLib (Video4j is used to handle OpenCV Mat)
		Video4j.init();
		SimpleImageViewer viewer = new SimpleImageViewer();

		try (InspirefaceSession session = InspirefaceLib.session(DEFAULT_PACK, 640)) {

			// Open the video using Video4j
			try (VideoFile video = VideoFile.open("/extra/vid/1.avi")) {

				// Process each frame
				VideoFrame frame;
				while ((frame = video.frame()) != null) {
					// System.out.println(frame);

					// Optionally downscale the frame
					CVUtils.resize(frame, 512);

					// Run the detection on the mat reference
					FaceDetections detections = session.detect(frame.mat(), true);

					if (!detections.isEmpty()) {
						// Extract the face embedding from the first face
						float[] embedding = session.embedding(frame.mat(), detections, 0);
						// Extract the face attributes
						List<FaceAttributes> attrs = session.attributes(frame.mat(), detections, true);
					}

					// Print the detections
					for (Detection detection : detections) {
						double confidence = detection.conf();
						BoundingBox box = detection.box();
						System.out.println("Frame[" + video.currentFrame() + "] = " + confidence + " @ " + box);
					}

					viewer.show(frame.mat());
				}
			}
		}
		// SNIPPET END video-usage.example
	}

}
