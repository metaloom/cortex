package io.metaloom.loom.test.assertj;

import org.assertj.core.api.Assertions;

import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;

public class LoomWorkerAssertions extends Assertions {

	public static ActionResultAssert assertThat(ActionResult actual) {
		return new ActionResultAssert(actual);
	}

	public static ProcessableMediaAssert assertThat(ProcessableMedia actual) {
		return new ProcessableMediaAssert(actual);
	}
}
