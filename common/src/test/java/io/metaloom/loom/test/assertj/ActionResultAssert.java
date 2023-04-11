package io.metaloom.loom.test.assertj;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.api.AbstractAssert;

import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ResultState;

public class ActionResultAssert extends AbstractAssert<ActionResultAssert, ActionResult> {

	protected ActionResultAssert(ActionResult actual) {
		super(actual, ActionResultAssert.class);
	}

	public ActionResultAssert isProcessed() {
		assertEquals( ResultState.PROCESSED, actual.getState(), "The action was not in stat processed.");
		return this;
	}

	public ActionResultAssert isSkipped() {
		assertEquals( ResultState.SKIPPED, actual.getState(), "The action was not in stat skipped.");
		return this;
	}
}
