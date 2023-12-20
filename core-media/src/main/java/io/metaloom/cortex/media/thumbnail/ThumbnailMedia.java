package io.metaloom.cortex.media.thumbnail;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

import java.io.OutputStream;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.param.ThumbnailFlag;
import io.metaloom.cortex.media.thumbnail.impl.ThumbnailMediaType;

public interface ThumbnailMedia extends LoomMedia {

	public static final ThumbnailMediaType THUMBNAIL = new ThumbnailMediaType();

	public static final LoomMetaKey<ThumbnailFlag> THUMBNAIL_FLAG_KEY = metaKey("thumbnail_flags", 1, XATTR, ThumbnailFlag.class);

	default ThumbnailFlag getThumbnailFlags() {
		return get(THUMBNAIL_FLAG_KEY);
	}

	default ThumbnailMedia setThumbnailFlag(ThumbnailFlag flag) {
		put(THUMBNAIL_FLAG_KEY, flag);
		return this;
	}

	default boolean hasThumbnail() {
		return false;
	}

	default OutputStream thumbnailOutputStream() {
		return null;
	}
}
