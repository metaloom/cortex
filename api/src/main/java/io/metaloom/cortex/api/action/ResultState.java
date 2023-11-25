package io.metaloom.cortex.api.action;

/**
 * Result state for an action that processed a media.
 */
public enum ResultState {

	/**
	 * Processing has been skipped
	 */
	SKIPPED,

	/**
	 * Processing has failed
	 */
	FAILED,

	/**
	 * Processing was successful.
	 */
	SUCCESS;
}
