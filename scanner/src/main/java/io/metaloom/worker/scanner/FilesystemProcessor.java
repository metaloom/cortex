package io.metaloom.worker.scanner;

import java.io.IOException;
import java.nio.file.Path;

import io.metaloom.fs.linux.LinuxFilesystemScanner;
import io.metaloom.worker.action.api.FilesystemAction;

/**
 * Processor which can handle media from filesystem.
 */
public interface FilesystemProcessor {

	void analyze(Path path) throws IOException;

	void registerAction(FilesystemAction action);

	LinuxFilesystemScanner getScanner();

}
