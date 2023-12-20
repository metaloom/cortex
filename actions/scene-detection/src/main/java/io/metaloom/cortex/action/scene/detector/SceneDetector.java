package io.metaloom.cortex.action.scene.detector;

import io.metaloom.cortex.media.scene.SceneDetectionResult;
import io.metaloom.video4j.VideoFile;

public interface SceneDetector {

	/**
	 * Run the scene detection for the provided video.
	 * 
	 * @param video
	 * @return detections
	 */
	SceneDetectionResult detect(VideoFile video);

}
