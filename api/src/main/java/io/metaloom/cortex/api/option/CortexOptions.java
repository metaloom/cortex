package io.metaloom.cortex.api.option;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import io.metaloom.cortex.api.option.action.CortexActionOptions;

public class CortexOptions {

	private Map<String, CortexActionOptions> actions = new HashMap<>();
	private LoomOptions loom = new LoomOptions();
	private boolean dryrun;

	private Path metaPath;

	public CortexOptions() {
	}

	public Map<String, CortexActionOptions> getActions() {
		return actions;
	}

	public CortexOptions setActions(Map<String, CortexActionOptions> actions) {
		this.actions = actions;
		return this;
	}

	public LoomOptions getLoom() {
		return loom;
	}

	public CortexOptions setLoom(LoomOptions loom) {
		this.loom = loom;
		return this;
	}

	public boolean isDryrun() {
		return dryrun;
	}

	public CortexOptions setDryrun(boolean dryrun) {
		this.dryrun = dryrun;
		return this;
	}

	/**
	 * Basepath for the metadata storage files.
	 * 
	 * @return
	 */
	public Path getMetaPath() {
		return metaPath;
	}

	public CortexOptions setMetaPath(Path metaPath) {
		this.metaPath = metaPath;
		return this;
	}

}
