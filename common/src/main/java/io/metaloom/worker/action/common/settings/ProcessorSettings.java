package io.metaloom.worker.action.common.settings;

public class ProcessorSettings {

	private int port;
	private String hostname;

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
}
