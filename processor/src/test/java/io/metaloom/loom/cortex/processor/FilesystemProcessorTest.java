package io.metaloom.loom.cortex.processor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.action.CortexActionOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.cortex.common.media.LoomMediaComponent;
import io.metaloom.cortex.common.media.LoomMediaLoader;
import io.metaloom.cortex.scanner.FilesystemProcessor;
import io.metaloom.cortex.scanner.impl.FilesystemProcessorImpl;
import io.metaloom.fs.linux.LinuxFilesystemScanner;
import io.metaloom.fs.linux.impl.LinuxFilesystemScannerImpl;
import io.metaloom.loom.rest.model.asset.AssetResponse;
import io.metaloom.loom.test.LocalTestData;

public class FilesystemProcessorTest {

	@BeforeAll
	public static void reset() throws IOException {
		LocalTestData.resetXattr();
	}

	@Test
	public void testProcessor() throws IOException {
		LinuxFilesystemScanner scanner = new LinuxFilesystemScannerImpl();
		FilesystemProcessor processor = new FilesystemProcessorImpl(scanner, Set.of(dummyAction()), mockLoader());
		processor.analyze(null, LocalTestData.localDir());
	}

	private FilesystemAction<?> dummyAction() throws IOException {
		FilesystemAction<?> action = new AbstractMediaAction<CortexActionOptions>(null, null, null) {

			@Override
			public CortexActionOptions options() {
				CortexActionOptions options = mock(CortexActionOptions.class);
				when(options.isEnabled()).thenReturn(true);
				return options;
			}

			@Override
			public String name() {
				return "dummy";
			}

			@Override
			protected boolean isProcessable(ActionContext ctx) {
				return true;
			}

			@Override
			protected boolean isProcessed(ActionContext ctx) {
				return false;
			}

			@Override
			protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws Exception {
				return ActionResult.processed(true);
			}
		};
		return action;
	}

	private LoomMediaLoader mockLoader() {
		LoomMediaComponent.Builder mediaBuilder = mock(LoomMediaComponent.Builder.class);

		// Just fake the binding of the instance
		when(mediaBuilder.loomMediaPath(Mockito.any())).thenAnswer(invocation -> {
			LoomMedia mediaMock = mock(LoomMedia.class);
			LoomMediaComponent mediaComponent = mock(LoomMediaComponent.class);
			Path path = invocation.getArgument(0, Path.class);
			when(mediaMock.path()).thenReturn(path);
			when(mediaBuilder.build()).thenReturn(mediaComponent);
			when(mediaComponent.media()).thenReturn(mediaMock);
			return mediaBuilder;
		});

		return new LoomMediaLoader(() -> mediaBuilder);
	}
}
