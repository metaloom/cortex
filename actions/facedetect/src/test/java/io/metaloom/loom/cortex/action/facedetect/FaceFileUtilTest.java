package io.metaloom.loom.cortex.action.facedetect;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Rectangle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.metaloom.loom.cortex.action.facedetect.assertj.FaceAssert;
import io.metaloom.loom.cortex.action.facedetect.file.FaceFileUtil;
import io.metaloom.video.facedetect.Face;
import io.metaloom.video4j.proto.FaceData;
import io.metaloom.video4j.proto.FaceEntry;

public class FaceFileUtilTest {

	@Test
	public void testWriteRead() throws IOException {
		Path testPath = Files.createTempFile(Paths.get("target"), "face-util-test", "readwrite");

		// Construct test data
		List<Face> faces = new ArrayList<>();
		Rectangle box = new Rectangle(20, 20, 200, 200);
		Face face = Face.create(box);
		face.setLabel("Albert Einstein");
		face.setEmbeddings(new float[] { 0.42f, 0.43f });
		face.set("age", "76");
		faces.add(face);

		// Write
		FaceFileUtil.writeFaceFile(testPath, faces);
		assertTrue(Files.exists(testPath));

		// Read
		FaceData data = FaceFileUtil.readFaceData(testPath);
		assertEquals(1, data.getFacesList().size());
		FaceEntry readFaceEntry = data.getFacesList().get(0);
		FaceAssert.assertThat(face).matches(readFaceEntry);

		// Read #2
		List<Face> readFaces = FaceFileUtil.readFaceFile(testPath);
		Face readFace = readFaces.get(0);
		FaceAssert.assertThat(face).matches(readFace);

	}

}
