package io.metaloom.cortex.media.ocr.impl;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;
import io.metaloom.cortex.media.ocr.OCRMedia;
import io.metaloom.cortex.media.ocr.impl.OCRMediaImpl;

public class OCRMediaType implements MediaType<OCRMedia> {

	public final static OCRMediaType OCR = new OCRMediaType();

	@Override
	public OCRMedia wrap(LoomMedia loomMedia) {
		return new OCRMediaImpl(loomMedia);
	}

}
