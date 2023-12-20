package io.metaloom.cortex.action.thumbnail.impl;

import io.metaloom.cortex.action.thumbnail.ThumbnailMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;

public class ThumbnailMediaType implements MediaType<ThumbnailMedia> {

	@Override
	public ThumbnailMedia wrap(LoomMedia loomMedia) {
		return new ThumbnailMediaImpl(loomMedia);
	}

}
