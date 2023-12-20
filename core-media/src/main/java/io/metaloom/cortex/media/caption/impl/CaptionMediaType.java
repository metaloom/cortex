package io.metaloom.cortex.media.caption.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;
import io.metaloom.cortex.media.caption.CaptionMedia;

public class CaptionMediaType implements MediaType<CaptionMedia> {

	@Override
	public CaptionMedia wrap(LoomMedia loomMedia) {
		return new CaptionMediaImpl(loomMedia);
	}

}
