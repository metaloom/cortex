package io.metaloom.cortex.common.option;

import io.metaloom.cortex.api.option.action.CortexActionOptions;

public class CortexActionOptionDeserializerInfo {

	private Class<? extends CortexActionOptions> clazz;
	private String prefix;

	public CortexActionOptionDeserializerInfo(Class<? extends CortexActionOptions> clazz, String prefix) {
		this.clazz = clazz;
		this.prefix = prefix;
	}

	public Class<? extends CortexActionOptions> getOptionClazz() {
		return clazz;
	}

	public String getOptionPrefix() {
		return prefix;
	}
}
