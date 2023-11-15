package io.metaloom.loom.cortex.action.facedetect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.metaloom.loom.cortex.action.facedetect.file.FaceFileUtil;
import io.metaloom.video.facedetect.Face;
import io.metaloom.video4j.proto.FaceData;

public class FaceFileUtilTest {

	private File testFile = new File("target/data.bin");

	@Test
	public void testWrite() throws IOException {
		List<Face> faces = new ArrayList<>();
		Rectangle box = new Rectangle(20, 20, 200, 200);
		Face face = Face.create(box);
		faces.add(face);
		FaceFileUtil.writeFaceFile(testFile, faces);
	}

	@Test
	public void testRead() throws FileNotFoundException, IOException {
		FaceData data = FaceFileUtil.readFaceData(testFile);
		assertEquals(1, data.getFacesList().size());
	}
}
