
package io.metaloom.cortex.media.whisper.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;
import io.metaloom.cortex.media.whisper.WhisperMedia;

public class WhisperMediaImpl extends AbstractMediaWrapper implements WhisperMedia {

	public WhisperMediaImpl(LoomMedia media) {
		super(media);
	}

}
