package io.metaloom.loom.worker.processor;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.metaloom.worker.processor.MediaProcessor;
import io.metaloom.worker.processor.impl.DefaultMediaProcessorImpl;
import io.metaloom.worker.scanner.FilesystemProcessorSetting;

public class DefaultProcessorTest {

	@BeforeAll
	public static void setupDir() throws IOException {
		LocalTestData.thumbnailDir().toFile().mkdirs();
		LocalTestData.resetXattr();
	}

	@Test
	public void testProcessor() throws IOException {
		FilesystemProcessorSetting settings = new FilesystemProcessorSetting();
		settings.getThumbnailSettings().setThumbnailPath(LocalTestData.thumbnailDir().toString());
		MediaProcessor processor = new DefaultMediaProcessorImpl(settings);
		processor.process(LocalTestData.localDir());
	}
}
