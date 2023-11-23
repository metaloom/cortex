package io.metaloom.cortex.api.action.media.action;

import static io.metaloom.cortex.api.action.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.action.media.LoomMetaType.XATTR;

import io.metaloom.cortex.api.action.media.LoomMetaKey;
import io.metaloom.cortex.api.action.media.ProcessableMedia;

public interface SceneDetectionMedia extends ProcessableMedia {

	public static final LoomMetaKey<String> SCENE_DETECTION_FLAG_KEY = metaKey("scene-detection-flags", 1, XATTR, String.class);

}
