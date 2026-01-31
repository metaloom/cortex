package io.metaloom.cortex.action.whisper;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class WhisperOptions extends AbstractActionOptions<WhisperOptions> {

	public static final String KEY = "whisper";

	private String modelPath = "models/ggml-large-v3-turbo.bin";

	private float temperature = 0.0f;

	private float temperatureInc = 0.2f;

	private String language;

	@Override
	protected WhisperOptions self() {
		return this;
	}

	public String getModelPath() {
		return modelPath;
	}

	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public float getTemperatureInc() {
		return temperatureInc;
	}

	public void setTemperatureInc(float temperatureInc) {
		this.temperatureInc = temperatureInc;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
