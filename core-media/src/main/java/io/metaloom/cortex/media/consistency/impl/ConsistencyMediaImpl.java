package io.metaloom.cortex.media.consistency.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;
import io.metaloom.cortex.media.consistency.ConsistencyMedia;

public class ConsistencyMediaImpl extends AbstractMediaWrapper implements ConsistencyMedia {

	public ConsistencyMediaImpl(LoomMedia loomMedia) {
		super(loomMedia);
	}

}
