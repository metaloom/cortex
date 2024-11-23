package io.metaloom.loom.cortex.processor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.metaloom.cortex.action.hash.HashActionOptions;
import io.metaloom.cortex.action.hash.SHA512Action;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.media.LoomMediaLoader;
import io.metaloom.cortex.common.media.impl.LoomMediaImpl;
import io.metaloom.cortex.processor.MediaProcessor;
import io.metaloom.cortex.processor.impl.DefaultMediaProcessorImpl;
import io.metaloom.cortex.scanner.FilesystemProcessor;
import io.metaloom.cortex.scanner.impl.FilesystemProcessorImpl;
import io.metaloom.fs.linux.LinuxFilesystemScanner;
import io.metaloom.fs.linux.impl.LinuxFilesystemScannerImpl;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.test.LocalTestData;

public class DefaultProcessorTest {

	@BeforeAll
	public static void setupDir() throws IOException {
		LocalTestData.metaStorageDir().toFile().mkdirs();
		LocalTestData.resetXattr();
	}

	@Test
	public void testProcessor() throws IOException {
		LoomClient client = null;
		CortexOptions options = new CortexOptions();
		options.setMetaPath(LocalTestData.metaStorageDir());
		//ThumbnailActionOptions thumbnailOptions = new ThumbnailActionOptions();
		Set<FilesystemAction> actions = Set.of(new SHA512Action(null, options, new HashActionOptions()));
		LinuxFilesystemScanner scanner = new LinuxFilesystemScannerImpl();
		LoomMediaLoader loader = mock(LoomMediaLoader.class);
		
		//MetaStorage storage = new MetaStorageImpl(null)
		LoomMedia media = new LoomMediaImpl(null, null);
		when(loader.load(Mockito.any())).thenReturn(media);
		FilesystemProcessor  fsProcessor = new FilesystemProcessorImpl(scanner, actions, loader);
		MediaProcessor processor = new DefaultMediaProcessorImpl(options, client, fsProcessor);
		processor.process(null, LocalTestData.localDir());
	}
}
