package io.metaloom.cortex.action.media;

import io.metaloom.cortex.action.media.source.MediaSource;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.utils.hash.ChunkHash;
import io.metaloom.utils.hash.MD5;
import io.metaloom.utils.hash.SHA256;
import io.metaloom.utils.hash.SHA512;

public interface VideoMediaSource extends MediaSource {

	default LoomMedia sampleVideoMedia() {
		return media(data().sampleVideoPath());
	}

	default LoomMedia sampleImageMedia1() {
		return media(data().sampleImage1Path());
	}

	default LoomMedia sampleVideoMedia2() {
		return media(data().sampleVideo2Path());
	}

	default LoomMedia sampleVideoMedia3() {
		return media(data().sampleVideo3Path());
	}

	default ChunkHash sampleVideoChunkHash() {
		return data().sampleVideoChunkHash();
	}
	
	default MD5 sampleVideoMD5() {
		return data().sampleVideoMD5();
	}

	default SHA512 sampleVideoSHA512() {
		return data().sampleVideoSHA512();
	}

	default SHA256 sampleVideoSHA256() {
		return data().sampleVideoSHA256();
	}
}
