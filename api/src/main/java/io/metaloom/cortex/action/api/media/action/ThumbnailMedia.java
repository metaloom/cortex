package io.metaloom.cortex.action.api.media.action;

import static io.metaloom.cortex.action.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.action.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.action.api.media.LoomMetaKey;
import io.metaloom.cortex.action.api.media.ProcessableMedia;

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
