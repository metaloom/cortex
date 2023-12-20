package io.metaloom.cortex.media.thumbnail.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;
import io.metaloom.cortex.media.thumbnail.ThumbnailMedia;

public class ThumbnailMediaType implements MediaType<ThumbnailMedia> {

	@Override
	public ThumbnailMedia wrap(LoomMedia loomMedia) {
		return new ThumbnailMediaImpl(loomMedia);
	}

}
