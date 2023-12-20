package io.metaloom.cortex.media.hash.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;
import io.metaloom.cortex.media.hash.HashMedia;

public class HashMediaType implements MediaType<HashMedia> {

	@Override
	public HashMedia wrap(LoomMedia loomMedia) {
		return new HashMediaImpl(loomMedia);
	}

}
