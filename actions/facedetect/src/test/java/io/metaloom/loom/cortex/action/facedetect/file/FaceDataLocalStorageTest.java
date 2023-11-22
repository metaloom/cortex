package io.metaloom.loom.cortex.action.facedetect.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.loom.cortex.action.facedetect.file.model.FaceData;
import io.metaloom.utils.hash.SHA512Sum;
import io.metaloom.video.facedetect.face.impl.FaceImpl;

public class FaceDataLocalStorageTest {

	public static final SHA512Sum HASH = SHA512Sum.fromString(
		"e7c22b994c59d9cf2b48e549b1e24666636045930d3da7c1acb299d1c3b7f931f94aae41edda2c2b207a36e10f8bcb8d45223e54878f5b316e7ce3b6bc019629");

	private final static Path BASE_DIR = Paths.get("target/base-storage");
	private final static FaceDataLocalStorage STORAGE = new FaceDataLocalStorage(BASE_DIR);

	@BeforeEach
	public void setup() throws IOException {
		FileUtils.deleteDirectory(BASE_DIR.toFile());
	}

	@Test
	public void testWriteRead() throws IOException {
		FaceData faceData = new FaceData();
		faceData.getEntries().add(new FaceImpl().setLabel("blub"));
		Path path = STORAGE.store(HASH, faceData);
		assertTrue(Files.exists(path));

		FaceData readData = STORAGE.load(HASH);
		assertNotNull(readData);
		assertEquals("blub", readData.getEntries().get(0).label());
	}

	@Test
	public void testMissingFile() throws IOException {
		assertNull(STORAGE.load(HASH));
	}
}
