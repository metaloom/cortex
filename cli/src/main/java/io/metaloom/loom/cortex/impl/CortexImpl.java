package io.metaloom.loom.cortex.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.api.option.CortexOptions;

@Singleton
public class CortexImpl implements Cortex {

	@Inject
	public CortexImpl(CortexOptions options) {
		System.out.println(options);
	}

}
