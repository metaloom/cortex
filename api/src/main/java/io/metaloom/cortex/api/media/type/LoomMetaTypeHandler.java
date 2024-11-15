package io.metaloom.cortex.api.media.type;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;

public interface LoomMetaTypeHandler {

	/**
	 * Type which can be handled.
	 * 
	 * @return
	 */
	LoomMetaType type();

	<T> void put(LoomMedia media, LoomMetaKey<T> metaKey, T value);

	<T> T get(LoomMedia media, LoomMetaKey<T> metaKey);

	<T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey);
}