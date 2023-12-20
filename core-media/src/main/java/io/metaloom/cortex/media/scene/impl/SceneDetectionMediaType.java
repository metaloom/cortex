package io.metaloom.cortex.media.scene.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;
import io.metaloom.cortex.media.scene.SceneDetectionMedia;

public class SceneDetectionMediaType implements MediaType<SceneDetectionMedia> {

	@Override
	public SceneDetectionMedia wrap(LoomMedia loomMedia) {
		return new SceneDetectionMediaImpl(loomMedia);
	}

}
