package io.metaloom.cortex.action.consistency.impl;

import io.metaloom.cortex.action.consistency.ConsistencyMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;

public class ConsistencyMediaImpl extends AbstractMediaWrapper implements ConsistencyMedia {

	public ConsistencyMediaImpl(LoomMedia loomMedia) {
		super(loomMedia);
	}

}
