package io.metaloom.cortex.action.scene;

import io.metaloom.cortex.action.scene.impl.SceneDetectionMediaImpl;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;

public class SceneDetectionMediaType implements MediaType<SceneDetectionMedia> {

	@Override
	public SceneDetectionMedia wrap(LoomMedia loomMedia) {
		return new SceneDetectionMediaImpl(loomMedia);
	}

}
