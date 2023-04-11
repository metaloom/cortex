package io.metaloom.loom.worker.processor;

import java.io.IOException;
import java.nio.file.Path;

import io.metaloom.worker.action.api.FilesystemAction;

/**
 * Processor which can handle media from filesystem.
 */
public interface FilesystemProcessor {

	void analyze(Path path) throws IOException;

	void registerAction(FilesystemAction action);

}
