package io.metaloom.cortex.action.scene;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class SceneDetectionOptions extends AbstractActionOptions<SceneDetectionOptions> {

	private boolean enabled = true;

	@Override
	protected SceneDetectionOptions self() {
		return this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean flag) {
		this.enabled = flag;
	}

}
