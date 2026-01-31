package io.metaloom.cortex.action.whisper;

public class AudioChunk {

	private float[] audio;

	public AudioChunk(float[] audio) {
		this.audio = audio;
	}

	public float[] getAudio() {
		return audio;
	}
}
