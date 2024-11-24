package io.metaloom.cortex.action.llm;

import java.util.HashMap;
import java.util.Map;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class LLMActionOptions extends AbstractActionOptions<LLMActionOptions> {

	private String ollamaUrl = "http://127.0.0.1:11434";

	private Map<String, LLMActionPrompt> prompts = new HashMap<>();

	public String ollamaUrl() {
		return ollamaUrl;
	}

	public void setOllamaUrl(String ollamaUrl) {
		this.ollamaUrl = ollamaUrl;
	}

	public Map<String, LLMActionPrompt> getPrompts() {
		return prompts;
	}

	public void setPrompts(Map<String, LLMActionPrompt> prompts) {
		this.prompts = prompts;
	}

	@Override
	protected LLMActionOptions self() {
		return this;
	}
}
