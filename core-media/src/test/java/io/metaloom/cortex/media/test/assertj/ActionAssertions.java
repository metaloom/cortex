package io.metaloom.cortex.media.test.assertj;

import org.assertj.core.api.Assertions;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;

public class ActionAssertions extends Assertions {

	public static ActionResultAssert assertThat(ActionResult actual) {
		return new ActionResultAssert(actual);
	}

	public static LoomMediaAssert assertThat(LoomMedia actual) {
		return new LoomMediaAssert(actual);
	}

}
