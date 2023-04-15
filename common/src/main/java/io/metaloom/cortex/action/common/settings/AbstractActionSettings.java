package io.metaloom.cortex.action.common.settings;

import io.metaloom.cortex.action.api.setting.ActionSettings;

public abstract class AbstractActionSettings implements ActionSettings {

	private boolean processIncomplete;

	private boolean retryFailed;

	public boolean isProcessIncomplete() {
		return processIncomplete;
	}

	public ActionSettings setProcessIncomplete(boolean processIncomplete) {
		this.processIncomplete = processIncomplete;
		return this;
	}

	public boolean isRetryFailed() {
		return retryFailed;
	}

	public ActionSettings setRetryFailed(boolean retryFailed) {
		this.retryFailed = retryFailed;
		return this;
	}

}
