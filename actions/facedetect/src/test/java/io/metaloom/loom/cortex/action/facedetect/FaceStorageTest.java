package io.metaloom.loom.cortex.action.facedetect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.facedetect.FaceStorage;
import io.metaloom.cortex.action.facedetect.file.model.FaceData;
import io.metaloom.loom.cortex.action.facedetect.assertj.FaceAssert;
import io.metaloom.utils.hash.SHA512;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video.facedetect.face.FaceBox;

public class FaceStorageTest implements FaceDataTest {

	private static final String SHA512_HASH = "3abb6677af34ac57c0ca5828fd94f9d886c26ce59a8ce60ecf6778079423dccff1d6f19cb655805d56098e6d38a1a710dee59523eed7511e5a9e4b8ccb3a4686";

	private Path testPath;
	private FaceStorage storage;

	@BeforeEach
	public void setup() throws IOException {
		Path dir = Paths.get("target", "face-stoage-test");
		Files.createDirectories(dir);
		testPath = Files.createTempDirectory(dir, "readwrite");
		storage = new FaceStorage(testPath);
	}

	@Test
	public void testWriteRead() throws IOException {
		assertTrue(Files.exists(testPath), "The storage dir should have been created");
		SHA512 hash = SHA512.fromString(SHA512_HASH);

		// Construct test data
		FaceData faceData = createFaceData(hash);

		// Write
		storage.store(faceData);

		// Read
		FaceData data = storage.read(hash);
		assertEquals(1, data.getEntries().size());
		Face readFace = data.getEntries().get(0);

		// System.out.println(readFace.box());
		FaceAssert.assertThat(faceData.getEntries().get(0)).matches(readFace);

	}

}
