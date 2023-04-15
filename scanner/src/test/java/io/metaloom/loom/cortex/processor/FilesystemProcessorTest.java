package io.metaloom.loom.cortex.processor;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.scanner.FilesystemProcessor;
import io.metaloom.cortex.scanner.impl.FilesystemProcessorImpl;

public class FilesystemProcessorTest {

	@BeforeAll
	public void reset() throws IOException {
		LocalTestData.resetXattr();
	}

	@Test
	public void testProcessor() throws IOException {
		FilesystemProcessor processor = new FilesystemProcessorImpl();
		processor.analyze(LocalTestData.localDir());
	}
}
