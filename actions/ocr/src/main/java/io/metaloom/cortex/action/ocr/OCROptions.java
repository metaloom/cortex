package io.metaloom.cortex.action.ocr;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class OCROptions extends AbstractActionOptions<OCROptions> {

	public static final String KEY = "ocr";

	@Override
	protected OCROptions self() {
		return this;
	}

}
