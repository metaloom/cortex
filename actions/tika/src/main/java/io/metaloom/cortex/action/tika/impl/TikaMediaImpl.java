package io.metaloom.cortex.action.tika.impl;

import io.metaloom.cortex.action.tika.TikaMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;

public class TikaMediaImpl extends AbstractMediaWrapper implements TikaMedia {

	public TikaMediaImpl(LoomMedia loomMedia) {
		super(loomMedia);
	}
}
