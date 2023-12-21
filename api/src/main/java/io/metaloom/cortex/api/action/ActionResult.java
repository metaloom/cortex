package io.metaloom.cortex.api.action;

public class ActionResult {

	public static final boolean CONTINUE_NEXT = true;
	public static final boolean SKIP_NEXT = false;

	private final boolean continueNext;
	private final ResultState state;
	private long duration = 0;

	public ActionResult(boolean continueNext, ResultState state) {
		this.continueNext = continueNext;
		this.state = state;
	}

	public ResultState getState() {
		return state;
	}

	public void setStart(long start) {
		this.duration = System.currentTimeMillis() - start;
	}

	/**
	 * Check whether further processing should stop or continue with next actions.
	 * 
	 * @return false indicates that the action want's to skip further processing
	 */
	public boolean isContinueNext() {
		return continueNext;
	}

	public long getDuration() {
		return duration;
	}

	public static ActionResult processed(boolean continueNext) {
		return new ActionResult(continueNext, ResultState.SUCCESS);
	}

	public static ActionResult failed(boolean continueNext) {
		return new ActionResult(continueNext, ResultState.FAILED);
	}

	public static ActionResult skipped(boolean continueNext) {
		return new ActionResult(continueNext, ResultState.SKIPPED);
	}

	@Override
	public String toString() {
		return state.name() + ", next: " + isContinueNext();
	}
}