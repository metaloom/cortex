package io.metaloom.cortex.action.api;

public interface CortexAction {

	/**
	 * Name of the action.
	 * 
	 * @return
	 */
	String name();

	boolean isDryrun();
}
