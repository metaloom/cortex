package io.metaloom.cortex.action.facedetect.impl;

import io.metaloom.cortex.action.facedetect.FacedetectMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;

public class FacedetectMediaImpl extends AbstractMediaWrapper implements FacedetectMedia {

	public FacedetectMediaImpl(LoomMedia media) {
		super(media);
	}

}
