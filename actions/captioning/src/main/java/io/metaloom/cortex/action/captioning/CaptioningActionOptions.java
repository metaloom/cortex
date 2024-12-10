package io.metaloom.cortex.action.captioning;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class CaptioningActionOptions extends AbstractActionOptions<CaptioningActionOptions> {

	private int smolVLMPort = 8000;
	private String smolVLMHost = "localhost";

	@Override
	protected CaptioningActionOptions self() {
		return this;
	}

	public int getSmolVLMPort() {
		return smolVLMPort;
	}

	public void setSmolVLMPort(int smolVLMPort) {
		this.smolVLMPort = smolVLMPort;
	}

	public String getSmolVLMHost() {
		return smolVLMHost;
	}

	public void setSmolVLMHost(String smolVLMHost) {
		this.smolVLMHost = smolVLMHost;
	}

}
