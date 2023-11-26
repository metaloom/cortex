package io.metaloom.cortex.action.scene;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.scene.detector.SceneDetector;
import io.metaloom.cortex.action.scene.impl.FeatureSceneDetector;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.data.TestDataCollection;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;

public class FeatureSceneDetectorTest {

	static {
		Video4j.init();
	}

	@Test
	public void testDetection() throws IOException {
		SceneDetector dectector = new FeatureSceneDetector();
		TestDataCollection data = TestEnvHelper.prepareTestdata("scene-detection-test");
		Path videoPath = data.video2().path();
		videoPath = Paths.get("/extra/vid/3.avi");
		try (VideoFile video = Videos.open(videoPath)) {
			dectector.detect(video);
		}
	}
}
