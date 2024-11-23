package io.metaloom.cortex.scanner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import io.metaloom.fs.linux.LinuxFilesystemScanner;

/**
 * Processor which can handle media from filesystem.
 */
public interface FilesystemProcessor {

	void analyze(List<String> enabledActions, Path path) throws IOException;

	LinuxFilesystemScanner getScanner();

}
