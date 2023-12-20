package io.metaloom.cortex.action.fp.impl;

import io.metaloom.cortex.action.fingerprint.FingerprintMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;

public class FingerprintMediaType implements MediaType<FingerprintMedia>{

	@Override
	public FingerprintMedia wrap(LoomMedia loomMedia) {
		return new FingerprintMediaImpl(loomMedia);
	}
}
