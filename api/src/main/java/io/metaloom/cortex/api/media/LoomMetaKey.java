package io.metaloom.cortex.api.media;

import io.metaloom.cortex.api.media.impl.LoomMetaKeyImpl;

public interface LoomMetaKey<T> {

	public static <T> LoomMetaKey<T> metaKey(String key, int version, LoomMetaType type, Class<T> valueClazz) {
		return new LoomMetaKeyImpl<T>(key, version, type, valueClazz);
	}

	int version();

	String key();

	/**
	 * Return the fully prefixed key which also contains the loom version and loom prefix.
	 * 
	 * @return
	 */
	String fullKey();

	LoomMetaType type();

	Class<?> getValueClazz();
}
