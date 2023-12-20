package io.metaloom.cortex.media.fingerprint.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;
import io.metaloom.cortex.media.fingerprint.FingerprintMedia;

public class FingerprintMediaType implements MediaType<FingerprintMedia>{

	@Override
	public FingerprintMedia wrap(LoomMedia loomMedia) {
		return new FingerprintMediaImpl(loomMedia);
	}
}
