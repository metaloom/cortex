package io.metaloom.loom.cortex.processor;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.thumbnail.ThumbnailActionOptions;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.processor.MediaProcessor;
import io.metaloom.cortex.processor.impl.DefaultMediaProcessorImpl;
import io.metaloom.loom.test.LocalTestData;

public class DefaultProcessorTest {

	@BeforeAll
	public static void setupDir() throws IOException {
		LocalTestData.metaStorageDir().toFile().mkdirs();
		LocalTestData.resetXattr();
	}

	@Test
	public void testProcessor() throws IOException {
		CortexOptions options = new CortexOptions();
		options.setMetaPath(LocalTestData.metaStorageDir());
		//ThumbnailActionOptions thumbnailOptions = new ThumbnailActionOptions();
	//	Set<CortexAction> actions = Sets.newHashSet(new DummyAction(null, options, null));
		MediaProcessor processor = new DefaultMediaProcessorImpl(options, null);
		processor.process(LocalTestData.localDir());
	}
}
