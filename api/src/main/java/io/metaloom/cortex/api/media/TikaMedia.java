package io.metaloom.cortex.api.media;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

public interface TikaMedia extends ProcessableMedia {

	public static final LoomMetaKey<String> TIKA_FLAGS_KEY = metaKey("tika_flags", 1, XATTR, String.class);

	default String getTikaFlags() {
		return get(TIKA_FLAGS_KEY);
	}

	default TikaMedia setTikaFlags(String flags) {
		put(TIKA_FLAGS_KEY, flags);
		return this;
	}

}
