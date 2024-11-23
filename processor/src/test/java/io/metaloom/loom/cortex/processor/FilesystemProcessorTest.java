package io.metaloom.loom.cortex.processor;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.scanner.FilesystemProcessor;
import io.metaloom.cortex.scanner.impl.FilesystemProcessorImpl;
import io.metaloom.loom.test.LocalTestData;

public class FilesystemProcessorTest {

	@BeforeAll
	public void reset() throws IOException {
		LocalTestData.resetXattr();
	}

	@Test
	public void testProcessor() throws IOException {
		FilesystemProcessor processor = new FilesystemProcessorImpl(null, null, null);
		processor.analyze(null, LocalTestData.localDir());
	}
}
