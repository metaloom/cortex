package io.metaloom.loom.worker.processor;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import io.metaloom.worker.scanner.FilesystemProcessor;
import io.metaloom.worker.scanner.impl.FilesystemProcessorImpl;

public class FilesystemProcessorTest {

	@Test
	public void testProcessor() throws IOException {
		FilesystemProcessor processor = new FilesystemProcessorImpl();
		processor.analyze(Paths.get("/tank3/input/pending"));
	}
}
