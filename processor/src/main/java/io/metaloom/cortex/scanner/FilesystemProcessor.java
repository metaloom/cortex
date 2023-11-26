package io.metaloom.cortex.scanner;

import java.io.IOException;
import java.nio.file.Path;

import io.metaloom.fs.linux.LinuxFilesystemScanner;

/**
 * Processor which can handle media from filesystem.
 */
public interface FilesystemProcessor {

	void analyze(Path path) throws IOException;

	LinuxFilesystemScanner getScanner();

}
