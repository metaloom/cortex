package io.metaloom.loom.cortex.action.facedetect;

import java.util.ArrayList;
import java.util.List;

import io.metaloom.loom.cortex.action.facedetect.avro.Facedetection;
import io.metaloom.loom.cortex.action.facedetect.avro.FacedetectionBox;
import io.metaloom.utils.FloatUtils;
import io.metaloom.utils.hash.SHA512;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video.facedetect.face.FaceBox;

public interface FaceDataTest {

	default public Facedetection createFaceData(SHA512 hash) {
		List<Face> faces = new ArrayList<>();
		FaceBox box = FaceBox.create(20, 20, 200, 200);
		Face face = Face.create(box);
		// face.setLabel("Albert Einstein");
		face.setEmbedding(new float[] { 0.42f, 0.43f });
		// face.set("age", "76");
		faces.add(face);

		int nFrame = 42;

		Facedetection f = Facedetection.newBuilder()
			.setAssetHash(hash.toString())
			.setFrame(nFrame)
			.setBox(toBox(box))
			.setEmbedding(FloatUtils.toList(face.getEmbedding()))
			.build();
		// FaceData faceData = new FaceData(hash);
		// faceData.setEntries(faces);
		return f;
	}

	static FacedetectionBox toBox(FaceBox box) {
		return FacedetectionBox.newBuilder()
			.setHeight(box.getHeight())
			.setWidth(box.getWidth())
			.setStartX(box.getStartX())
			.setStartY(box.getStartY())
			.build();
	}

}
