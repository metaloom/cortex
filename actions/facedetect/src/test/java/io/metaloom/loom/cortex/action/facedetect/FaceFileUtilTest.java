package io.metaloom.loom.cortex.action.facedetect;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.facedetect.file.FaceFileUtil;
import io.metaloom.cortex.action.facedetect.file.model.FaceData;
import io.metaloom.loom.cortex.action.facedetect.assertj.FaceAssert;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video.facedetect.face.FaceBox;

public class FaceFileUtilTest {

	@Test
	public void testWriteRead() throws IOException {
		Path testPath = Files.createTempFile(Paths.get("target"), "face-util-test", "readwrite");

		// Construct test data
		List<Face> faces = new ArrayList<>();
		FaceBox box = FaceBox.create(20, 20, 200, 200);
		Face face = Face.create(box);
		face.setLabel("Albert Einstein");
		face.setEmbedding(new float[] { 0.42f, 0.43f });
		face.set("age", "76");
		faces.add(face);

		FaceData faceData = new FaceData();
		faceData.setEntries(faces);

		// Write
		FaceFileUtil.writeFaceFile(testPath, faceData);
		assertTrue(Files.exists(testPath));

		// Read
		FaceData data = FaceFileUtil.readFaceData(testPath);
		assertEquals(1, data.getEntries().size());
		Face readFace = data.getEntries().get(0);
		//System.out.println(readFace.box());
		FaceAssert.assertThat(face).matches(readFace);

	}

}
