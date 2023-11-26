package io.metaloom.cortex.api.action;

import io.metaloom.cortex.api.option.action.CortexActionOptions;

public interface CortexAction {

	/**
	 * Name of the action.
	 * 
	 * @return
	 */
	String name();

	boolean isDryrun();

	CortexActionOptions options();
}
