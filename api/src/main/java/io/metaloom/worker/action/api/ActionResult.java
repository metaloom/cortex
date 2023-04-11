package io.metaloom.worker.action.api;

public class ActionResult {

	public static final boolean CONTINUE_NEXT = true;
	public static final boolean SKIP_NEXT = false;

	private boolean continueNext = false;
	private long duration = 0;
	private ResultState state;

	public ActionResult(boolean continueNext, long duration, ResultState state) {
		this.continueNext = continueNext;
		this.duration = duration;
		this.state = state;
	}

	public ResultState getState() {
		return state;
	}

	/**
	 * 
	 * @return false indicates that the action want's to skip further processing
	 */
	public boolean isContinueNext() {
		return continueNext;
	}

	public long getDuration() {
		return duration;
	}

	public static ActionResult processed(boolean continueNext, long start) {
		long duration = System.currentTimeMillis() - start;
		return new ActionResult(continueNext, duration, ResultState.PROCESSED);
	}

	public static ActionResult failed(boolean continueNext, long start) {
		long duration = System.currentTimeMillis() - start;
		return new ActionResult(continueNext, duration, ResultState.FAILED);
	}

	public static ActionResult skipped(boolean continueNext, long start) {
		long duration = System.currentTimeMillis() - start;
		return new ActionResult(continueNext, duration, ResultState.SKIPPED);
	}
}