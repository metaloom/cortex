package io.metaloom.cortex.media.fingerprint.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;
import io.metaloom.cortex.media.fingerprint.FingerprintMedia;

public class FingerprintMediaImpl extends AbstractMediaWrapper implements FingerprintMedia {

	public FingerprintMediaImpl(LoomMedia media) {
		super(media);
	}

}
