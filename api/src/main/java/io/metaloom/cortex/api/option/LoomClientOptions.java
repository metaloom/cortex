package io.metaloom.cortex.api.option;

public class LoomClientOptions {

	private String hostname;
	private int port;

	public int getPort() {
		return port;
	}

	public LoomClientOptions setPort(int port) {
		this.port = port;
		return this;
	}

	public String getHostname() {
		return hostname;
	}

	public LoomClientOptions setHostname(String hostname) {
		this.hostname = hostname;
		return this;
	}

}
