package io.metaloom.cortex.media.scene;

import java.util.ArrayList;
import java.util.List;

public class SceneDetectionResult {

	private List<Scene> scenes = new ArrayList<>();

	public void addScene(Scene scene) {
		scenes.add(scene);
	}

	public List<Scene> scenes() {
		return scenes;
	}

}
