package io.metaloom.loom.worker.processor;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;

import io.metaloom.loom.worker.processor.impl.FilesystemProcessorImpl;

public class FilesystemProcessorTest {

	@Test
	public void testProcessor() throws IOException {
		FilesystemProcessor processor = new FilesystemProcessorImpl();
		processor.analyze(Paths.get("target/test-folder"));
	}
}
