package io.metaloom.loom.test.assertj;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.metaloom.cortex.api.action.media.LoomMedia;

public class LoomMediaAssert extends AbstractProcessableMediaAssert<LoomMediaAssert, LoomMedia> {

	protected LoomMediaAssert(LoomMedia actual) {
		super(actual, LoomMediaAssert.class);
	}

	@Override
	protected LoomMediaAssert self() {
		return this;
	}

	public LoomMediaAssert hasSHA256() {
		assertNotNull(actual.getSHA256());
		return this;
	}

	public LoomMediaAssert hasSHA512() {
		assertNotNull(actual.getSHA512());
		return this;
	}

}
