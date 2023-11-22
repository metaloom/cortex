package io.metaloom.loom.cortex.action.facedetect;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.metaloom.loom.cortex.action.facedetect.video.VideoFaceScanner;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.utils.SimpleImageViewer;

public class VideoFaceClusterDetectorTest {

	private SimpleImageViewer viewer = new SimpleImageViewer();

	VideoFaceScanner detector = new VideoFaceScanner();
	// private static final int WINDOW_COUNT = 30;
	// private static final int WINDOW_SIZE = 120;
	// private static final int WINDOW_STEPS = 5;

	private static final int WINDOW_COUNT = 30;
	private static final int WINDOW_SIZE = 30;
	private static final int WINDOW_STEPS = 5;

	@Test
	public void testExampleCode() throws InterruptedException, IOException {

		try (VideoFile video = Videos.open("/extra/vid/6.mkv")) {
			long start = System.currentTimeMillis();
			List<Face>  faces = detector.scan(video, WINDOW_COUNT, WINDOW_SIZE, WINDOW_STEPS);
			long dur = System.currentTimeMillis() - start;
			System.out.println("Scan took " + dur + " ms / " + (dur / 1000) + " s");
		}

	}

	// @Test
	// public void testWindowScan() throws InterruptedException {
	// try (VideoFile video = Videos.open("/extra/vid/4.mkv")) {
	// List<FrameWindow> windows = detector.locateWindows(video, 2);
	// detector.fineTuneWindow(video, windows.get(0));
	// }
	//
	// }
}
