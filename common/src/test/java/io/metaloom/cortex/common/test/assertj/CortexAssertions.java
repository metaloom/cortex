package io.metaloom.cortex.common.test.assertj;

import org.assertj.core.api.Assertions;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;

public class CortexAssertions extends Assertions {

	public static ActionResultAssert assertThat(ActionResult actual) {
		return new ActionResultAssert(actual);
	}

	public static LoomMediaAssert assertThat(LoomMedia actual) {
		return new LoomMediaAssert(actual);
	}
}
