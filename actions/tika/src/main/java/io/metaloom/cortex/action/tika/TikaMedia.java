package io.metaloom.cortex.action.tika;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.action.tika.impl.TikaMediaType;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;

public interface TikaMedia extends LoomMedia {

	public static final TikaMediaType TIKA = new TikaMediaType();

	public static final LoomMetaKey<String> TIKA_FLAGS_KEY = metaKey("tika_flags", 1, XATTR, String.class);

	default String getTikaFlags() {
		return get(TIKA_FLAGS_KEY);
	}

	default void setTikaFlags(String flags) {
		put(TIKA_FLAGS_KEY, flags);
	}

}
