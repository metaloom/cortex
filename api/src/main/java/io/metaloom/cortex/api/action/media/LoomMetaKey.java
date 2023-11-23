package io.metaloom.cortex.api.action.media;

import io.metaloom.cortex.api.action.media.impl.LoomMetaKeyImpl;

public interface LoomMetaKey<T> {

	public static <T> LoomMetaKey<T> metaKey(String key, int version, LoomMetaType type, Class<T> valueClazz) {
		return new LoomMetaKeyImpl<T>(key, version, type, valueClazz);
	}

	int version();

	String key();

	String fullKey();

	LoomMetaType type();

	Class<?> getValueClazz();
}
