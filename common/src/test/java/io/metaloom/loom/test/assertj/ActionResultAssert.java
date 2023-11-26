package io.metaloom.loom.test.assertj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.assertj.core.api.AbstractAssert;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.ResultState;

public class ActionResultAssert extends AbstractAssert<ActionResultAssert, ActionResult2> {

	protected ActionResultAssert(ActionResult2 actual) {
		super(actual, ActionResultAssert.class);
	}

	public ActionResultAssert isProcessed() {
		assertEquals(ResultState.SUCCESS, actual.getState(), "The action was not in stat processed.");
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
