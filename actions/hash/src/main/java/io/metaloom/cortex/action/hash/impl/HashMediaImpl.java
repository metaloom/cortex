package io.metaloom.cortex.action.hash.impl;

import io.metaloom.cortex.action.hash.HashMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;

public class HashMediaImpl extends AbstractMediaWrapper implements HashMedia {

	public HashMediaImpl(LoomMedia media) {
		super(media);
	}

}
