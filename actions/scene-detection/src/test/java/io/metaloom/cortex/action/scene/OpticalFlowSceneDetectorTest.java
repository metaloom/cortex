package io.metaloom.cortex.action.scene;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.scene.impl.OpticalFlowSceneDetector;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.Testdata;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;

public class OpticalFlowSceneDetectorTest {

	static {
		Video4j.init();
	}

	@Test
	public void testDetection() throws IOException {
		SceneDetector dectector = new OpticalFlowSceneDetector();
		Testdata data = TestEnvHelper.prepareTestdata("scene-detection-test");
		Path videoPath = data.sampleVideo2Path();
		videoPath = Paths.get("/extra/vid/7.mkv");
		try (VideoFile video = Videos.open(videoPath)) {
			dectector.detect(video);
		}
	}
}
