package io.metaloom.cortex.action.scene;

import io.metaloom.video4j.VideoFile;

public interface SceneDetector {

	/**
	 * Run the scene detection for the provided video.
	 * 
	 * @param video
	 */
	void detect(VideoFile video);

}
