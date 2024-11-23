package io.metaloom.cortex.action.llm;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class LLMActionOptions extends AbstractActionOptions<LLMActionOptions> {

	String ollamaUrl;

	public String ollamaUrl() {
		return ollamaUrl;
	}

	public void setOllamaUrl(String ollamaUrl) {
		this.ollamaUrl = ollamaUrl;
	}

	@Override
	protected LLMActionOptions self() {
		return this;
	}
}
