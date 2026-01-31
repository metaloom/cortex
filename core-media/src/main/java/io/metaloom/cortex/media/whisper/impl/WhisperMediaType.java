package io.metaloom.cortex.media.whisper.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;
import io.metaloom.cortex.media.whisper.WhisperMedia;

public class WhisperMediaType implements MediaType<WhisperMedia> {

	@Override
	public WhisperMedia wrap(LoomMedia loomMedia) {
		return new WhisperMediaImpl(loomMedia);
	}

}
