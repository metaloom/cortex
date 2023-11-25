package io.metaloom.cortex.action.media.source;

import io.metaloom.cortex.api.action.media.LoomMedia;

public interface OtherMediaSource extends MediaSource {

	default LoomMedia sampleBogusBin() {
		return media(data().sampleBogusBin());
	}
	
	default LoomMedia missingMP4() {
		return media(data().missingMP4());
	}
}
