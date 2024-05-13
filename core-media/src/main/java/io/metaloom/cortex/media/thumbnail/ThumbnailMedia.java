package io.metaloom.cortex.media.thumbnail;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.FS;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.param.ThumbnailFlag;
import io.metaloom.cortex.api.meta.MetaDataStream;
import io.metaloom.cortex.media.thumbnail.impl.ThumbnailMediaType;

public interface ThumbnailMedia extends LoomMedia {

	public static final ThumbnailMediaType THUMBNAIL = new ThumbnailMediaType();

	public static final LoomMetaKey<ThumbnailFlag> THUMBNAIL_FLAG_KEY = metaKey("thumbnail_flags", 1, XATTR, ThumbnailFlag.class);

	public static final LoomMetaKey<MetaDataStream> THUMBNAIL_BIN_KEY = metaKey("thumbnail_bin", 1, FS, MetaDataStream.class);

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

}
