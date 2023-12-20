package io.metaloom.cortex.action.fp.impl;

import io.metaloom.cortex.action.fp.FingerprintMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;

public class FingerprintMediaImpl extends AbstractMediaWrapper implements FingerprintMedia {

	public FingerprintMediaImpl(LoomMedia media) {
		super(media);
	}

}
