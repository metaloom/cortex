package io.metaloom.cortex.action.hash.impl;

import io.metaloom.cortex.action.hash.HashMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;

public class HashMediaType implements MediaType<HashMedia> {

	@Override
	public HashMedia wrap(LoomMedia loomMedia) {
		return new HashMediaImpl(loomMedia);
	}

}
