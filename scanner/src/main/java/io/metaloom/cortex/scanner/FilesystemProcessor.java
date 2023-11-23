package io.metaloom.cortex.scanner;

import java.io.IOException;
import java.nio.file.Path;

import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.fs.linux.LinuxFilesystemScanner;

/**
 * Processor which can handle media from filesystem.
 */
public interface FilesystemProcessor {

	void analyze(Path path) throws IOException;

	void registerAction(FilesystemAction action);

	LinuxFilesystemScanner getScanner();

}
