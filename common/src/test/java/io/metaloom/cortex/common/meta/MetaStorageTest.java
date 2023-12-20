package io.metaloom.cortex.common.meta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.LoomMetaType;
import io.metaloom.cortex.api.media.impl.LoomMetaKeyImpl;
import io.metaloom.cortex.api.media.param.BSONAttr;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;

public class MetaStorageTest extends AbstractMediaTest {

	private static final LoomMetaKey<String> DUMMY_FS_STR_KEY = new LoomMetaKeyImpl<>("dummy_1", 0, LoomMetaType.FS, String.class);

	private static final LoomMetaKey<DummyBSONValue> DUMMY_FS_BSON_KEY = new LoomMetaKeyImpl<>("dummy_2", 0, LoomMetaType.FS, DummyBSONValue.class);

	private static final LoomMetaKey<String> DUMMY_XATTR_STR_KEY = new LoomMetaKeyImpl<>("dummy_3", 0, LoomMetaType.XATTR, String.class);

	private static final LoomMetaKey<DummyBSONValue> DUMMY_XATTR_BSON_KEY = new LoomMetaKeyImpl<>("dummy_4", 0, LoomMetaType.XATTR,
		DummyBSONValue.class);

	private static final LoomMetaKey<String> DUMMY_HEAP_STR_KEY = new LoomMetaKeyImpl<>("dummy_5", 0, LoomMetaType.HEAP, String.class);

	private static final LoomMetaKey<DummyBSONValue> DUMMY_HEAP_BSON_KEY = new LoomMetaKeyImpl<>("dummy_6", 0, LoomMetaType.HEAP,
		DummyBSONValue.class);

	private MetaStorage storage;

	@BeforeEach
	public void setupMetaStorage() throws IOException {
		CortexOptions options = new CortexOptions();
		options.setMetaPath(Files.createTempDirectory("meta"));
		storage = new MetaStorageImpl(options);
	}

	@Test
	public void testOutputStream() throws IOException {
		testWriteValueViaOS(DUMMY_FS_STR_KEY, "Hello World");
		testWriteValueViaOS(DUMMY_XATTR_STR_KEY, "Hello World");
		testWriteValueViaOS(DUMMY_HEAP_STR_KEY, "Hello World");
	}

	@Test
	public void testHeapKey() {
		testPutHasGet(DUMMY_HEAP_STR_KEY, "Hello World");
		testPutHasGet(DUMMY_HEAP_BSON_KEY, new DummyBSONValue().setName("bson"));
	}

	@Test
	public void testXAttrKey() {
		testPutHasGet(DUMMY_XATTR_STR_KEY, "Hello World");
		testPutHasGet(DUMMY_XATTR_BSON_KEY, new DummyBSONValue().setName("bson"));
	}

	@Test
	public void testFSKey() {
		testPutHasGet(DUMMY_FS_STR_KEY, "Hello World");
		testPutHasGet(DUMMY_FS_BSON_KEY, new DummyBSONValue().setName("bson"));
	}

	private <T> void testWriteValueViaOS(LoomMetaKey<T> metaKey, T value) throws IOException {
		LoomMedia media = mediaVideo1();
		assertFalse(storage.has(media, metaKey));
		try (OutputStream os = storage.outputStream(media, metaKey)) {
			os.write(value.toString().getBytes());
		}
		assertTrue(storage.has(media, metaKey));
	}

	private <T> void testPutHasGet(LoomMetaKey<T> metaKey, T value) {
		LoomMedia media = mediaVideo1();
		assertFalse(storage.has(media, metaKey));
		storage.put(media, metaKey, value);
		assertTrue(storage.has(media, metaKey));
		T readValue = storage.get(media, metaKey);
		assertEquals(value, readValue, "The read back value does not match for " + metaKey);
	}

	public MetaStorage storage() {
		return storage;
	}

	class DummyBSONValue implements BSONAttr {
		String name = "";

		public String getName() {
			return name;
		}

		public DummyBSONValue setName(String name) {
			this.name = name;
			return this;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof DummyBSONValue v) {
				return v.name.equals(name);
			}
			return false;
		}
	}

}
