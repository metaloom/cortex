package io.metaloom.cortex.action.media.source;

import io.metaloom.cortex.api.action.media.LoomMedia;

public interface ImageMediaSource extends MediaSource {

	default LoomMedia sampleImageMedia() {
		return media(data().sampleImage1Path());
	}

}
