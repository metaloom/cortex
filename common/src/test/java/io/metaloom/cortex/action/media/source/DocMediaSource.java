package io.metaloom.cortex.action.media.source;

import io.metaloom.cortex.api.action.media.LoomMedia;

public interface DocMediaSource extends MediaSource {

	default LoomMedia sampleDOCX() {
		return media(data().sampleDOCX());
	}

	default LoomMedia samplePDF() {
		return media(data().samplePDF());
	}

	default LoomMedia sampleTXT() {
		return media(data().sampleTXT());
	}

	default LoomMedia sampleODT() {
		return media(data().sampleODT());
	}

	default LoomMedia sampleEPUB() {
		return media(data().sampleEPUB());
	}

}
