package io.metaloom.cortex.action.captioning.impl;

import io.metaloom.cortex.action.captioning.CaptionMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;

public class CaptionMediaImpl extends AbstractMediaWrapper implements CaptionMedia {

	public CaptionMediaImpl(LoomMedia media) {
		super(media);
	}

}
