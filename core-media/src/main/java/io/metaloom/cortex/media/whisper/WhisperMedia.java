package io.metaloom.cortex.media.whisper;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.type.LoomMetaCoreType.FS;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.media.whisper.impl.WhisperMediaType;

public interface WhisperMedia extends LoomMedia {

	public static final WhisperMediaType WHISPER = new WhisperMediaType();

	public static final LoomMetaKey<WhisperResult> WHISPER_FLAG_KEY = metaKey("whisper-result", 1, FS,
		WhisperResult.class);

	default boolean hasWhisper() {
		return has(WHISPER_FLAG_KEY);
	}

	default WhisperResult getWhisperResult() {
		return get(WHISPER_FLAG_KEY);
	}

	default void setWhisperResult(WhisperResult result) {
		put(WHISPER_FLAG_KEY, result);
	}

}
