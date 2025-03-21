package io.metaloom.cortex.common.action;

import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.api.option.action.CortexActionOptions;
import io.metaloom.loom.client.common.LoomClient;

public abstract class AbstractCortexAction<T extends CortexActionOptions> implements CortexAction<T> {

	private final LoomClient client;
	private final CortexOptions cortexOption;
	private final T option;

	public AbstractCortexAction(LoomClient client, CortexOptions cortexOption, T option) {
		this.client = client;
		this.cortexOption = cortexOption;
		this.option = option;
	}

	@Override
	public T options() {
		return option;
	}

	@Override
	public CortexOptions cortexOption() {
		return cortexOption;
	}

	public boolean isOfflineMode() {
		return client == null;
	}

	protected LoomClient client() {
		return client;
	}

	@Override
	public boolean isDryrun() {
		return cortexOption().isDryrun();
	}

}
