package io.metaloom.cortex.api.option;

import java.util.HashMap;
import java.util.Map;

import io.metaloom.cortex.api.option.action.CortexActionOptions;

public class CortexOptions {

	private String loomHost;
	private int loomPort;

	private Map<String, CortexActionOptions> actions = new HashMap<>();
	private ProcessorSettings processorSettings = new ProcessorSettings();

	public CortexOptions() {
	}

	public Map<String, CortexActionOptions> getActions() {
		return actions;
	}

	public void setActions(Map<String, CortexActionOptions> actions) {
		this.actions = actions;
	}

	public String getLoomHost() {
		return loomHost;
	}

	public void setLoomHost(String loomHost) {
		this.loomHost = loomHost;
	}

	public int getLoomPort() {
		return loomPort;
	}

	public void setLoomPort(int loomPort) {
		this.loomPort = loomPort;
	}

	public ProcessorSettings getProcessorSettings() {
		return processorSettings;
	}
}
