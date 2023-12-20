package io.metaloom.cortex.media.hash.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;
import io.metaloom.cortex.media.hash.HashMedia;

public class HashMediaImpl extends AbstractMediaWrapper implements HashMedia {

	public HashMediaImpl(LoomMedia media) {
		super(media);
	}

}
