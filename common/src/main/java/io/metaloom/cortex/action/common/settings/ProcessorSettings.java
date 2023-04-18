package io.metaloom.cortex.action.common.settings;

public class ProcessorSettings {

	private int port;
	private String hostname;

	private boolean dryrun;

	public int getPort() {
		return port;
	}

	public ProcessorSettings setPort(int port) {
		this.port = port;
		return this;
	}

	public String getHostname() {
		return hostname;
	}

	public ProcessorSettings setHostname(String hostname) {
		this.hostname = hostname;
		return this;
	}

	public boolean isDryrun() {
		return dryrun;
	}

	public ProcessorSettings setDryrun(boolean dryrun) {
		this.dryrun = dryrun;
		return this;
	}
}
