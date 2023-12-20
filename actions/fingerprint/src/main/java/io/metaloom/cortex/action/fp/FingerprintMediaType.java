package io.metaloom.cortex.action.fp;

import io.metaloom.cortex.action.fp.impl.FingerprintMediaImpl;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;

public class FingerprintMediaType implements MediaType<FingerprintMedia>{

	@Override
	public FingerprintMedia wrap(LoomMedia loomMedia) {
		return new FingerprintMediaImpl(loomMedia);
	}
}
