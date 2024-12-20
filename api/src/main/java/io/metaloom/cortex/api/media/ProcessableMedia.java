package io.metaloom.cortex.api.media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import io.metaloom.cortex.api.meta.MetaStorageAccess;

/**
 * Media which will be processable via cortex actions.
 */
public interface ProcessableMedia extends MetaStorageAccess {

	boolean isVideo();

	boolean isImage();

	boolean isAudio();

	boolean isDocument();

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

	void setPath(Path path);

	/**
	 * Size in bytes.
	 * 
	 * @return
	 * @throws IOException
	 */
	long size() throws IOException;

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

}
