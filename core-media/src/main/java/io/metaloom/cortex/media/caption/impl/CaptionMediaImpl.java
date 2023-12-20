package io.metaloom.cortex.media.caption.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;
import io.metaloom.cortex.media.caption.CaptionMedia;

public class CaptionMediaImpl extends AbstractMediaWrapper implements CaptionMedia {

	public CaptionMediaImpl(LoomMedia media) {
		super(media);
	}

}
