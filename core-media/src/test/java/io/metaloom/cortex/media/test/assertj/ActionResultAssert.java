package io.metaloom.cortex.media.test.assertj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.assertj.core.api.AbstractAssert;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.ResultState;

public class ActionResultAssert extends AbstractAssert<ActionResultAssert, ActionResult> {

	protected ActionResultAssert(ActionResult actual) {
		super(actual, ActionResultAssert.class);
	}

	public ActionResultAssert isSuccess() {
		assertEquals(ResultState.SUCCESS, actual.getState(), "The action was not in stat success.");
		return this;
	}

	public ActionResultAssert isSkipped() {
		assertEquals(ResultState.SKIPPED, actual.getState(), "The action was not in stat skipped.");
		return this;
	}

	public ActionResultAssert isFailed() {
		assertEquals(ResultState.FAILED, actual.getState(), "The action was not in stat failed.");
		return this;
	}

	public void isContinueNext() {
		assertTrue(actual.isContinueNext(), "The continue next flag was not true");
	}
}
