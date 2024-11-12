package io.metaloom.cortex.api.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

//	/**
//	 * Return an output stream to write to for the given key + media.
//	 * 
//	 * @param <T>
//	 * @param media
//	 * @param metaKey
//	 * @return
//	 * @throws IOException
//	 */
//	<T> OutputStream outputStream(LoomMedia media, LoomMetaKey<T> metaKey) throws IOException;
//
//	/**
//	 * Return an input stream to read for the given key + media.
//	 * 
//	 * @param <T>
//	 * @param media
//	 * @param metaKey
//	 * @return
//	 */
//	<T> InputStream inputStream(LoomMedia media, LoomMetaKey<T> metaKey) throws IOException;

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

	void setSHA512(LoomMedia media, SHA512 hash);

	SHA512 getSHA512(LoomMedia media);

}
