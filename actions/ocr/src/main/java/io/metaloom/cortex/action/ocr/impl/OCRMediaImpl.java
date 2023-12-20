package io.metaloom.cortex.action.ocr.impl;

import io.metaloom.cortex.action.ocr.OCRMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.meta.AbstractMediaWrapper;

public class OCRMediaImpl extends AbstractMediaWrapper implements OCRMedia {

	public OCRMediaImpl(LoomMedia media) {
		super(media);
	}

}
