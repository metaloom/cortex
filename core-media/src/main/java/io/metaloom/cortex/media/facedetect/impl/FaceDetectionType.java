package io.metaloom.cortex.media.facedetect.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;
import io.metaloom.cortex.media.facedetect.FacedetectMedia;

public class FaceDetectionType implements MediaType<FacedetectMedia> {

	@Override
	public FacedetectMedia wrap(LoomMedia loomMedia) {
		return new FacedetectMediaImpl(loomMedia);
	}

}
