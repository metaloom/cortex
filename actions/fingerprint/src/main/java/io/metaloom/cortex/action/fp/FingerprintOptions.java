package io.metaloom.cortex.action.fp;

import javax.inject.Inject;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class FingerprintOptions extends AbstractActionOptions<FingerprintOptions> {

	@Inject
	public FingerprintOptions() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected FingerprintOptions self() {
		return this;
	}

}
