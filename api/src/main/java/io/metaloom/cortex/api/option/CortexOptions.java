package io.metaloom.cortex.api.option;

import io.metaloom.cortex.api.option.action.ActionOptions;

public class CortexOptions {

	private String loomHost;
	private int loomPort;

	private ActionOptions actions = new ActionOptions();
	private ProcessorSettings processorSettings = new ProcessorSettings();

	public CortexOptions() {
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

	public ActionOptions getActions() {
		return actions;
	}

	public ProcessorSettings getProcessorSettings() {
		return processorSettings;
	}
}
