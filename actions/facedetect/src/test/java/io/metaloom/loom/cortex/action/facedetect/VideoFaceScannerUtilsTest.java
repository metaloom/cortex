package io.metaloom.loom.cortex.action.facedetect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.facedetect.video.FrameWindow;
import io.metaloom.cortex.action.facedetect.video.VideoFaceScannerUtils;

public class VideoFaceScannerUtilsTest {

	@Test
	public void testSplitWindows() {
		assertEquals(1, VideoFaceScannerUtils.splitWindows(150, 15, 40).size());
		assertEquals(37, VideoFaceScannerUtils.splitWindows(15_000L, 15, 40).size());

		for (FrameWindow window : VideoFaceScannerUtils.splitWindows(15_000L, 15, 100)) {
			System.out.println(window);
		}
	}

}
