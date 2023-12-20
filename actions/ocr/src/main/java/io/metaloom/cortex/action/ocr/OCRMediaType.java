package io.metaloom.cortex.action.ocr;

import io.metaloom.cortex.action.ocr.impl.OCRMediaImpl;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.MediaType;

public class OCRMediaType implements MediaType<OCRMedia> {

	public final static OCRMediaType OCR = new OCRMediaType();

	@Override
	public OCRMedia wrap(LoomMedia loomMedia) {
		return new OCRMediaImpl(loomMedia);
	}

}
