package io.metaloom.cortex.media.whisper;

import java.util.ArrayList;
import java.util.List;

public class WhisperResult {

	private List<TranscriptionSegment> scenes = new ArrayList<>();

	public void addTranscriptionSegment(TranscriptionSegment scene) {
		scenes.add(scene);
	}

	public List<TranscriptionSegment> segments() {
		return scenes;
	}

}
