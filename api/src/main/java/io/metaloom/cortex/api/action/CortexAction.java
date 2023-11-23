package io.metaloom.cortex.api.action;

public interface CortexAction {

	/**
	 * Name of the action.
	 * 
	 * @return
	 */
	String name();

	boolean isDryrun();
}
