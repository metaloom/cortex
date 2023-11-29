package io.metaloom.cortex.api.media;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

public interface SceneDetectionMedia extends ProcessableMedia {

	public static final LoomMetaKey<String> SCENE_DETECTION_FLAG_KEY = metaKey("scene-detection-flags", 1, XATTR, String.class);

}
