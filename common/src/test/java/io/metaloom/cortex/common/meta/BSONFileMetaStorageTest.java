package io.metaloom.cortex.common.meta;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.meta.MetaStorageKey;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.cortex.common.meta.impl.BSONFileMetaStorage;

public class BSONFileMetaStorageTest extends AbstractMediaTest {

	private MetaStorage storage;

	@BeforeEach
	public void setupMetaStorage() throws IOException {
		CortexOptions options = new CortexOptions();
		options.setMetaPath(Files.createTempDirectory("meta"));
		storage = new BSONFileMetaStorage(options);
	}

	@Test
	public void testOutputStream() throws IOException {
		LoomMedia media = mockMedia();
		assertFalse(storage.has(media, dummyKey()));

		try (OutputStream os = storage.outputStream(media, dummyKey())) {
			os.write("hello world".getBytes());
		}

		assertTrue(storage.has(media, dummyKey()));
	}

	private LoomMedia mockMedia() {
		LoomMedia media = mock(LoomMedia.class);
		when(media.getSHA512()).thenReturn(HASH);
		return media;
	}

	private MetaStorageKey dummyKey() {
		return new MetaStorageKey() {
			@Override
			public String name() {
				return "dummy";
			}
		};
	}
}
