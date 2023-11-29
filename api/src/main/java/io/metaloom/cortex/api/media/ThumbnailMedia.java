package io.metaloom.cortex.api.media;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.api.media.param.ThumbnailFlag;

public interface ThumbnailMedia extends ProcessableMedia {

	public static final LoomMetaKey<ThumbnailFlag> THUMBNAIL_FLAG_KEY = metaKey("thumbnail_flags", 1, XATTR, ThumbnailFlag.class);

	default ThumbnailFlag getThumbnailFlags() {
		return get(THUMBNAIL_FLAG_KEY);
	}

	default ThumbnailMedia setThumbnailFlag(ThumbnailFlag flag) {
		put(THUMBNAIL_FLAG_KEY, flag);
		return this;
	}

}
