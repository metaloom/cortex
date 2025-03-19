package io.metaloom.cortex.action.facedescription;

import io.metaloom.ai.genai.llm.LLMProviderType;
import io.metaloom.ai.genai.llm.LargeLanguageModel;

public enum FaceDescriptionModel implements LargeLanguageModel {

	OLLAMA_GEMMA3_12B_Q8("gemma3:12b-it-q8_0", LLMProviderType.OLLAMA),

	OLLAMA_GEMMA3_27B_Q8("gemma3:27b-it-q8_0", LLMProviderType.OLLAMA);

	private String id;

	private LLMProviderType providerType;

	FaceDescriptionModel(String id, LLMProviderType type) {
		this.id = id;
		this.providerType = type;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public int contextWindow() {
		return 4096;
	}

	@Override
	public LLMProviderType providerType() {
		return providerType;
	}

	@Override
	public String url() {
		return "http://localhost:11434";
	}

}
