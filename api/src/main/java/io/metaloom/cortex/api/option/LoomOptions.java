package io.metaloom.cortex.api.option;

public class LoomOptions {

	private String hostname;
	private int port;

	public int getPort() {
		return port;
	}

	public LoomOptions setPort(int port) {
		this.port = port;
		return this;
	}

	public String getHostname() {
		return hostname;
	}

	public LoomOptions setHostname(String hostname) {
		this.hostname = hostname;
		return this;
	}

}
