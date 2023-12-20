package io.metaloom.cortex.action.thumbnail.impl;

import io.metaloom.cortex.action.thumbnail.ThumbnailMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;

public class ThumbnailMediaImpl extends AbstractMediaWrapper implements ThumbnailMedia {

	public ThumbnailMediaImpl(LoomMedia media) {
		super(media);
	}

}
