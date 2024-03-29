package io.metaloom.cortex.api.action;

import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.api.option.action.CortexActionOptions;

public interface CortexAction<T extends CortexActionOptions> {

	/**
	 * Name of the action.
	 * 
	 * @return
	 */
	String name();

	/**
	 * Return the configured order for the action.
	 * 
	 * @return
	 */
	default ActionOrder order() {
		return ActionOrder.UNDEFINED;
	}

	/**
	 * Check whether the action is configured to dryrun
	 * 
	 * @return
	 */
	boolean isDryrun();

	/**
	 * Return the options for the action.
	 * 
	 * @return
	 */
	T options();

	CortexOptions cortexOption();

	/**
	 * Initialize the action.
	 */
	void initialize();

}
