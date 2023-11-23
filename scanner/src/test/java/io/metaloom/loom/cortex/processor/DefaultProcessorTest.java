package io.metaloom.loom.cortex.processor;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.processor.MediaProcessor;
import io.metaloom.cortex.processor.impl.DefaultMediaProcessorImpl;
import io.metaloom.loom.test.LocalTestData;

public class DefaultProcessorTest {

	@BeforeAll
	public static void setupDir() throws IOException {
		LocalTestData.thumbnailDir().toFile().mkdirs();
		LocalTestData.resetXattr();
	}

	@Test
	public void testProcessor() throws IOException {
		CortexOptions options = new CortexOptions();
		options.getActions().getThumbnail().setThumbnailPath(LocalTestData.thumbnailDir().toString());
		MediaProcessor processor = new DefaultMediaProcessorImpl(options);
		processor.process(LocalTestData.localDir());
	}
}
