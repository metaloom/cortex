package io.metaloom.cortex.api.media;

public enum LoomMetaType {
	/**
	 * Store the metadata within the xattr of the file
	 */
	XATTR,

	/**
	 * Store the metadata in the filesystem.
	 */
	FS,

	/**
	 * Store the metadata only in HEAP of the current JVM
	 */
	HEAP;
}
