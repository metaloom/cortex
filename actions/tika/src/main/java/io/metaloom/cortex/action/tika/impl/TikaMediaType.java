package io.metaloom.cortex.action.tika.impl;

import io.metaloom.cortex.action.tika.TikaMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;

public class TikaMediaType implements MediaType<TikaMedia> {

	@Override
	public TikaMedia wrap(LoomMedia loomMedia) {
		return new TikaMediaImpl(loomMedia);
	}

}
