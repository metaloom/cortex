package io.metaloom.cortex.action.captioning.impl;

import io.metaloom.cortex.action.captioning.CaptionMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;

public class CaptionMediaType implements MediaType<CaptionMedia> {

	@Override
	public CaptionMedia wrap(LoomMedia loomMedia) {
		return new CaptionMediaImpl(loomMedia);
	}

}
