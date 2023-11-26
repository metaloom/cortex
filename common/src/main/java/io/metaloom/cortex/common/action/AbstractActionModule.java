package io.metaloom.cortex.common.action;

import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.api.option.action.CortexActionOptions;

public abstract class AbstractActionModule {

	public static <T extends CortexActionOptions> T actionOptions(CortexOptions cortexOptions, String key, T defaultOptions) {
		T options = (T) cortexOptions.getActions().get(key);
		if (options == null) {
			return defaultOptions;
		} else {
			return options;
		}
	}
}
