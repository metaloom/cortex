package io.metaloom.cortex.media.scene;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.type.LoomMetaCoreType.FS;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.media.scene.impl.SceneDetectionMediaType;

public interface SceneDetectionMedia extends LoomMedia {

	public static final SceneDetectionMediaType SCENE_DETECTION = new SceneDetectionMediaType();

	public static final LoomMetaKey<SceneDetectionResult> SCENE_DETECTION_FLAG_KEY = metaKey("scene-detection-result", 1, FS,
		SceneDetectionResult.class);

	default boolean hasSceneDetection() {
		return has(SCENE_DETECTION_FLAG_KEY);
	}

	default SceneDetectionResult getSceneDetection() {
		return get(SCENE_DETECTION_FLAG_KEY);
	}

	default void setSceneDetection(SceneDetectionResult result) {
		put(SCENE_DETECTION_FLAG_KEY, result);
	}

}
