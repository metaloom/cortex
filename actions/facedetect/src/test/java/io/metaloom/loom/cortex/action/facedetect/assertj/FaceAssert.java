package io.metaloom.loom.cortex.action.facedetect.assertj;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;

import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.api.AbstractAssert;

import io.metaloom.video.facedetect.Face;
import io.metaloom.video4j.proto.FaceEntry;

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

	public FaceAssert matches(FaceEntry entry) {
		assertEquals(entry.getStartX(), actual.box().x);
		assertEquals(entry.getStartY(), actual.box().y);

		assertEquals(entry.getWidth(), actual.box().width);
		assertEquals(entry.getHeight(), actual.box().height);

		assertEquals(entry.getLabel(), actual.label());

		Float[] readEmbedding = entry.getEmbeddingList().toArray(new Float[entry.getEmbeddingCount()]);
		float[] readEmbeddingF = ArrayUtils.toPrimitive(readEmbedding);
		assertArrayEquals(readEmbeddingF, actual.getEmbedding());
		return this;
	}

	public FaceAssert matches(Face face) {
		assertEquals(face.box().x, actual.box().x);
		assertEquals(face.box().y, actual.box().y);
		assertEquals(face.box().getWidth(), actual.box().width);
		assertEquals(face.box().getHeight(), actual.box().height);
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
