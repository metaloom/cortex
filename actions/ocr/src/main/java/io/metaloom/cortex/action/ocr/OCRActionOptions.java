package io.metaloom.cortex.action.ocr;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class OCRActionOptions extends AbstractActionOptions<OCRActionOptions> {

	public static final String KEY = "ocr";

	@Override
	protected OCRActionOptions self() {
		return this;
	}

}
