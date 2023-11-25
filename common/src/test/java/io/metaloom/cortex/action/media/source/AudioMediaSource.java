package io.metaloom.cortex.action.media.source;

import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.utils.hash.MD5;

public interface AudioMediaSource extends MediaSource {

	default MD5 sampleMD5() {
		return MD5.fromString("2c32d5bfdba2c9c15c12f3c87b0bc0d0");
	}

	default LoomMedia sampleAudioMedia() {
		return media(data().sampleAudioMedia());
	}
}
