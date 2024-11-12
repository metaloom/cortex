package io.metaloom.cortex.common.meta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashSet;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.impl.LoomMetaKeyImpl;
import io.metaloom.cortex.api.media.type.LoomMetaCoreType;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.api.media.type.handler.impl.AvroLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.media.type.handler.impl.FSLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.media.type.handler.impl.HeapLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.media.type.handler.impl.MetaStorageException;
import io.metaloom.cortex.api.media.type.handler.impl.XAttrLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.meta.MetaDataStream;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;

public class MetaStorageTest extends AbstractMediaTest {

	private static final LoomMetaKey<String> DUMMY_FS_STR_KEY = new LoomMetaKeyImpl<>("dummy_fs_str", 0, LoomMetaCoreType.FS, String.class);

	private static final LoomMetaKey<MetaDataStream> DUMMY_FS_BIN_KEY = new LoomMetaKeyImpl<>("dummy_fs_bin", 0, LoomMetaCoreType.FS,
		MetaDataStream.class);

	private static final LoomMetaKey<MetaDataStream> DUMMY_XATTR_BIN_KEY = new LoomMetaKeyImpl<>("dummy_xattr_bin", 0, LoomMetaCoreType.XATTR,
		MetaDataStream.class);

	private static final LoomMetaKey<String> DUMMY_XATTR_STR_KEY = new LoomMetaKeyImpl<>("dummy_xattr_str", 0, LoomMetaCoreType.XATTR, String.class);

	private static final LoomMetaKey<String> DUMMY_HEAP_STR_KEY = new LoomMetaKeyImpl<>("dummy_heap_str", 0, LoomMetaCoreType.HEAP, String.class);

	private static final LoomMetaKey<MetaDataStream> DUMMY_HEAP_BIN_KEY = new LoomMetaKeyImpl<>("dummy_heap_str", 0, LoomMetaCoreType.HEAP,
		MetaDataStream.class);

	private MetaStorage storage;

	@BeforeEach
	public void setupMetaStorage() throws IOException {
		CortexOptions options = new CortexOptions();
		options.setMetaPath(Files.createTempDirectory("meta"));

		HashSet<LoomMetaTypeHandler> handlers = new HashSet<>();
		handlers.add(new HeapLoomMetaTypeHandlerImpl());
		handlers.add(new AvroLoomMetaTypeHandlerImpl());
		handlers.add(new XAttrLoomMetaTypeHandlerImpl());
		handlers.add(new FSLoomMetaTypeHandlerImpl(options));
		storage = new MetaStorageImpl(options, handlers);
	}

	@Test
	public void testOutputFSStream() throws IOException {
		testWriteValueViaStream(DUMMY_FS_BIN_KEY, "Hello World");
	}

	@Test
	public void testHeapOutputStream() throws IOException {
		MetaStorageException msg = assertThrows(MetaStorageException.class, () -> {
			testWriteValueViaStream(DUMMY_HEAP_BIN_KEY, "Hello World");
		}, "Expected storage handler to fail since xattr does not support streams");

		assertEquals("Streams are not supported for XAttr", msg.getMessage());
	}

	@Test
	public void testOutputXAttrStream() {
		MetaStorageException msg = assertThrows(MetaStorageException.class, () -> {
			testWriteValueViaStream(DUMMY_XATTR_BIN_KEY, "Hello World");
		}, "Expected storage handler to fail since xattr does not support streams");

		assertEquals("Streams are not supported for XAttr", msg.getMessage());

	}

	@Test
	public void testHeapKey() {
		testPutHasGet(DUMMY_HEAP_STR_KEY, "Hello World");
	}

	@Test
	public void testXAttrKey() {
		testPutHasGet(DUMMY_XATTR_STR_KEY, "Hello World");
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
	}

	private void testWriteValueViaStream(LoomMetaKey<MetaDataStream> metaKey, String value) throws IOException {
		LoomMedia media = mediaVideo1();
		assertFalse(storage.has(media, metaKey));
		MetaDataStream stream = storage.get(media, metaKey);
		try (OutputStream os = stream.outputStream()) {
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

}
