package io.metaloom.loom.cortex.impl;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.api.option.CortexOptions;

@Singleton
public class CortexImpl implements Cortex {

	private final CortexOptions options;
	private final Set<CortexAction> actions;

	@Inject
	public CortexImpl(CortexOptions options, Set<CortexAction> actions) {
		this.options = options;
		this.actions = actions;
	}

	@Override
	public void checkActions() {
		for (CortexAction action : actions) {
			System.out.println(action.options());
		}
	}

}
