package io.metaloom.loom.test.assertj;

import org.assertj.core.api.Assertions;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.ProcessableMedia;

public class LoomWorkerAssertions extends Assertions {

	public static ActionResultAssert assertThat(ActionResult actual) {
		return new ActionResultAssert(actual);
	}

	public static ProcessableMediaAssert assertThat(ProcessableMedia actual) {
		return new ProcessableMediaAssert(actual);
	}
}
