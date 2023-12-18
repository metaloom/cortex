package io.metaloom.cortex.api.media.param;

import java.util.ArrayList;
import java.util.List;

public class SceneDetectionParameter implements BSONAttr {

	private List<DetectedScene> scenes =  new ArrayList<>();

	public List<DetectedScene> getScenes() {
		return scenes;
	}

	public SceneDetectionParameter setScenes(List<DetectedScene> scenes) {
		this.scenes = scenes;
		return this;
	}
}
