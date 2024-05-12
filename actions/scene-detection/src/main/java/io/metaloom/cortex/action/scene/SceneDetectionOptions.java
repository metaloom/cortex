package io.metaloom.cortex.action.scene;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class SceneDetectionOptions extends AbstractActionOptions<SceneDetectionOptions> {

	public static final String KEY = "scene-detector";

	@Override
	protected SceneDetectionOptions self() {
		return this;
	}

}
