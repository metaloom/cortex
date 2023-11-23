package io.metaloom.cortex.api.action.media.action;

import static io.metaloom.cortex.api.action.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.action.media.LoomMetaType.XATTR;

import io.metaloom.cortex.api.action.media.LoomMetaKey;
import io.metaloom.cortex.api.action.media.ProcessableMedia;

public interface ThumbnailMedia extends ProcessableMedia {

	public static final LoomMetaKey<String> THUMBNAIL_FLAGS_KEY = metaKey("thumbnail_flags", 1, XATTR, String.class);

	default String getThumbnailFlags() {
		return get(THUMBNAIL_FLAGS_KEY);
	}

	default ThumbnailMedia setThumbnailFlags(String flags) {
		put(THUMBNAIL_FLAGS_KEY, flags);
		return this;
	}

}
