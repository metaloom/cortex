package io.metaloom.cortex.action.facedetect;

import io.metaloom.cortex.action.facedetect.impl.FacedetectMediaImpl;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;

public class FaceDetectionType implements MediaType<FacedetectMedia> {

	@Override
	public FacedetectMedia wrap(LoomMedia loomMedia) {
		return new FacedetectMediaImpl(loomMedia);
	}

}
