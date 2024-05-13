package io.metaloom.cortex.api.media;

import java.nio.ByteBuffer;
import java.util.function.Function;

import io.metaloom.cortex.api.media.impl.LoomMetaKeyImpl;

public interface LoomMetaKey<T> {

	public static <T> LoomMetaKey<T> metaKey(String key, int version, LoomMetaType type, Class<T> valueClazz) {
		return new LoomMetaKeyImpl<T>(key, version, type, valueClazz);
	}

	public static <T> LoomMetaKey<T> metaKey(String key, int version, LoomMetaType type, Class<T> valueClazz, Function<ByteBuffer, T> creator) {
		return new LoomMetaKeyImpl<T>(key, version, type, valueClazz, creator);
	}

	int version();

	String key();

	T newValue(ByteBuffer b);

	/**
	 * Return the fully prefixed key which also contains the loom version and loom prefix.
	 * 
	 * @return
	 */
	String fullKey();

	LoomMetaType type();

	Class<T> getValueClazz();
}
