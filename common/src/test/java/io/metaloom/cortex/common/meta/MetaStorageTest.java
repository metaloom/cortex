package io.metaloom.cortex.common.meta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.LoomMetaType;
import io.metaloom.cortex.api.media.impl.LoomMetaKeyImpl;
import io.metaloom.cortex.api.media.param.BSONAttr;
import io.metaloom.cortex.api.meta.MetaDataStream;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;

public class MetaStorageTest extends AbstractMediaTest {

	private static final LoomMetaKey<String> DUMMY_FS_STR_KEY = new LoomMetaKeyImpl<>("dummy_fs_str", 0, LoomMetaType.FS, String.class);

	private static final LoomMetaKey<MetaDataStream> DUMMY_FS_BIN_KEY = new LoomMetaKeyImpl<>("dummy_fs_bin", 0, LoomMetaType.FS,
		MetaDataStream.class);

	private static final LoomMetaKey<DummyBSONValue> DUMMY_FS_BSON_KEY = new LoomMetaKeyImpl<>("dummy_fs_bson", 0, LoomMetaType.FS,
		DummyBSONValue.class);

	private static final LoomMetaKey<String> DUMMY_XATTR_STR_KEY = new LoomMetaKeyImpl<>("dummy_xattr_str", 0, LoomMetaType.XATTR, String.class);

	private static final LoomMetaKey<DummyBSONValue> DUMMY_XATTR_BSON_KEY = new LoomMetaKeyImpl<>("dummy_xattr_bson", 0, LoomMetaType.XATTR,
		DummyBSONValue.class);

	private static final LoomMetaKey<String> DUMMY_HEAP_STR_KEY = new LoomMetaKeyImpl<>("dummy_heap_str", 0, LoomMetaType.HEAP, String.class);

	private static final LoomMetaKey<DummyBSONValue> DUMMY_HEAP_BSON_KEY = new LoomMetaKeyImpl<>("dummy_heap_bson", 0, LoomMetaType.HEAP,
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
	public void testFSBinaryData() throws IOException {
		LoomMedia media = mediaVideo1();
		assertFalse(storage.has(media, DUMMY_FS_BIN_KEY));

		MetaDataStream stream = storage.get(media, DUMMY_FS_BIN_KEY);
		assertNotNull(stream, "The stream access should be returned even if the attribute has not yet been written");
		try (PrintWriter p = new PrintWriter(stream.outputStream())) {
			p.write("HelloWorld");
		}
		assertTrue(storage.has(media, DUMMY_FS_BIN_KEY));
		try (InputStream ins = stream.inputStream()) {
			String text = IOUtils.toString(ins, Charset.defaultCharset());
			assertEquals("HelloWorld", text, "The read back value does not match for " + DUMMY_FS_BIN_KEY);
		}
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
		assertTrue(storage.has(media, metaKey), "Media has no value for key " + metaKey);
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
