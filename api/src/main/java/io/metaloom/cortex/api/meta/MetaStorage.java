package io.metaloom.cortex.api.meta;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.utils.hash.SHA512;

public interface MetaStorage {

	/**
	 * Check whether there is a value for the given key + media.
	 * 
	 * @param <T>
	 * @param media
	 * @param metaKey
	 * @return
	 */
	<T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey);

	/**
	 * Return the meta property for the given key + media.
	 * 
	 * @param <T>
	 * @param media
	 * @param metaKey
	 * @return
	 */
	<T> T get(LoomMedia media, LoomMetaKey<T> metaKey);

	/**
	 * Store the value for the given key + media.
	 * 
	 * @param <T>
	 * @param media
	 * @param metaKey
	 * @param value
	 */
	<T> void put(LoomMedia media, LoomMetaKey<T> metaKey, T value);

	/**
	 * Append the value to the media.
	 * 
	 * @param <T>
	 * @param media
	 * @param metakey
	 * @param value
	 */
	<T> void append(LoomMedia media, LoomMetaKey<T> metakey, T value);

	void setSHA512(LoomMedia media, SHA512 hash);

	SHA512 getSHA512(LoomMedia media);

}
