package io.metaloom.cortex.api.media;	

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.FS;

import io.metaloom.cortex.api.media.param.SceneDetectionParameter;

public interface SceneDetectionMedia extends ProcessableMedia {

	public static final LoomMetaKey<SceneDetectionParameter> SCENE_DETECTION_FLAG_KEY = metaKey("scene-detection-flags", 1, FS,
		SceneDetectionParameter.class);

	default SceneDetectionParameter getSceneDetection() {
		return get(SCENE_DETECTION_FLAG_KEY);
	}

	default SceneDetectionMedia setSceneDetection(SceneDetectionParameter param) {
		put(SCENE_DETECTION_FLAG_KEY, param);
		return this;
	}

}
