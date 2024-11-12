package io.metaloom.cortex.api.media;

import java.nio.ByteBuffer;
import java.util.function.Function;

import io.metaloom.cortex.api.media.impl.LoomMetaKeyImpl;
import io.metaloom.cortex.api.media.type.LoomMetaCoreType;

public interface LoomMetaKey<T> {

	public static <T> LoomMetaKey<T> metaKey(String key, int version, LoomMetaCoreType type, Class<T> valueClazz) {
		return new LoomMetaKeyImpl<T>(key, version, type, valueClazz);
	}

	public static <T> LoomMetaKey<T> metaKey(String key, int version, LoomMetaCoreType type, Class<T> valueClazz, Function<ByteBuffer, T> creator) {
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

	/**
	 * Return the type for the key. The type is used to identify the handler which will be invoked to operate on the loom key.
	 * 
	 * @return
	 */
	LoomMetaCoreType type();

	Class<T> getValueClazz();
}
