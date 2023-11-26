package io.metaloom.cortex.action.dedup;

import java.nio.file.Path;
import java.nio.file.Paths;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class DedupActionOptions extends AbstractActionOptions<DedupActionOptions> {

	private static final String DEFAULT_DUP_FOLDER = "duplicates";

	private Path dupFolder = Paths.get(DEFAULT_DUP_FOLDER);

	public Path getDupFolder() {
		return dupFolder;
	}

	public DedupActionOptions setDupFolder(Path dupFolder) {
		this.dupFolder = dupFolder;
		return this;
	}

	@Override
	protected DedupActionOptions self() {
		return this;
	}

}
