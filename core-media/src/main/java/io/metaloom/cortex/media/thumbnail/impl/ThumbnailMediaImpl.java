package io.metaloom.cortex.media.thumbnail.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;
import io.metaloom.cortex.media.thumbnail.ThumbnailMedia;

public class ThumbnailMediaImpl extends AbstractMediaWrapper implements ThumbnailMedia {

	public ThumbnailMediaImpl(LoomMedia media) {
		super(media);
	}

}
