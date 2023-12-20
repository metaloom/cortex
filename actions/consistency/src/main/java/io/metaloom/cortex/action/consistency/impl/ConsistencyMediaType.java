package io.metaloom.cortex.action.consistency.impl;

import io.metaloom.cortex.action.consistency.ConsistencyMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;

public class ConsistencyMediaType implements MediaType<ConsistencyMedia> {

	@Override
	public ConsistencyMedia wrap(LoomMedia loomMedia) {
		return new ConsistencyMediaImpl(loomMedia);
	}

}
