package io.metaloom.loom.cortex.dedup;

import java.nio.file.Path;
import java.nio.file.Paths;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class DedupOptions extends AbstractActionOptions<DedupOptions> {

	private static final String DEFAULT_DUP_FOLDER = "duplicates";

	private Path dupFolder = Paths.get(DEFAULT_DUP_FOLDER);

	public Path getDupFolder() {
		return dupFolder;
	}

	public DedupOptions setDupFolder(Path dupFolder) {
		this.dupFolder = dupFolder;
		return this;
	}

	@Override
	protected DedupOptions self() {
		return this;
	}

}
