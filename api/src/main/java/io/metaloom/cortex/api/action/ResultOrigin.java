package io.metaloom.cortex.api.action;

public enum ResultOrigin {

	/**
	 * Result has been computed
	 */
	COMPUTED,

	/**
	 * Result has been fetched from local storage (xattr, local storage)
	 */
	LOCAL,

	/**
	 * Result has been fetched remotely via gRPC from loom.
	 */
	REMOTE

}
