package io.metaloom.loom.cortex.action.facedetect.assertj;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;

import org.assertj.core.api.AbstractAssert;

import io.metaloom.video.facedetect.face.Face;

public class FaceAssert extends AbstractAssert<FaceAssert, Face> {

	protected FaceAssert(Face actual) {
		super(actual, FaceAssert.class);
	}

	public static FaceAssert assertThat(Face actual) {
		return new FaceAssert(actual);
	}

	public FaceAssert hasLabel(String label) {
		isNotNull();
		assertEquals(label, actual.label());
		return this;
	}

	public FaceAssert matches(Face face) {
		assertEquals(face.box().getStartX(), actual.box().getStartX());
		assertEquals(face.box().getStartY(), actual.box().getStartY());
		assertEquals(face.box().getWidth(), actual.box().getWidth());
		assertEquals(face.box().getHeight(), actual.box().getHeight());
		assertEquals(face.label(), actual.label());
		assertArrayEquals(face.getEmbedding(), actual.getEmbedding());
		assertEquals(face.getLandmarks().size(), actual.getLandmarks().size());

		for (int i = 0; i < face.getLandmarks().size(); i++) {
			Point l1 = face.getLandmarks().get(i);
			Point l2 = actual.getLandmarks().get(i);
			assertEquals(l1, l2);
		}

		assertEquals(face.data().keySet(), actual.data().keySet());

		for (String key : face.data().keySet()) {
			Object v1 = face.get(key);
			Object v2 = actual.get(key);
			assertEquals(v1, v2, "Value for key " + key + " did not match");
		}
		return this;

	}
}
