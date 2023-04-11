package io.metaloom.worker.action.api;

import static io.metaloom.worker.action.api.ProcessableMediaMeta.SHA_512;
import static io.metaloom.worker.action.api.ProcessableMediaMeta.ZERO_CHUNK_COUNT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public interface ProcessableMedia {

	boolean isVideo();

	boolean isImage();

	boolean isAudio();

	/**
	 * Return the underlying file for the media.
	 * 
	 * @return
	 */
	File file();

	/**
	 * Return the filesystem path to the media.
	 * 
	 * @return
	 */
	Path path();

	/**
	 * Return the absolute filesystem path for the media.
	 * 
	 * @return
	 */
	String absolutePath();

	/**
	 * Check if the media exists.
	 * 
	 * @return
	 */
	boolean exists();

	/**
	 * Open the media stream.
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	InputStream open() throws FileNotFoundException;

	/**
	 * List the extended file attributes for the media file.
	 * 
	 * @return
	 */
	List<String> listXAttr();

	/**
	 * Return the meta property for the given key.
	 * 
	 * @param <T>
	 * @param key
	 * @param classOfT
	 * @return
	 */
	<T> T get(String key, Class<T> classOfT);

	default <T> T get(ProcessableMediaMeta meta, Class<T> classOfT) {
		return get(meta.key(), classOfT);
	}

	/**
	 * Return the meta property for the given type.
	 * 
	 * @param <T>
	 * @param meta
	 * @return
	 */
	default <T> T get(ProcessableMediaMeta meta) {
		Objects.requireNonNull(meta.type(), "The meta attribute has no specified type.");
		return (T) get(meta.key(), meta.type());
	}

	/**
	 * Write the value to the local cache and also to the xattr of the file when required.
	 * 
	 * @param key
	 * @param value
	 * @param writeToXattr
	 * @return
	 */
	ProcessableMedia put(String key, Object value, boolean writeToXattr);

	default ProcessableMedia put(ProcessableMediaMeta meta, Object value) {
		return put(meta.key(), value, meta.isPersisted());
	}

	default String getHash512() {
		return get(SHA_512);
	}

	default Boolean isComplete() {
		Long zeroChunks = get(ZERO_CHUNK_COUNT);
		if (zeroChunks != null) {
			return zeroChunks == 0;
		} else {
			return null;
		}
	}

}
