package io.metaloom.loom.cortex.action.facedetect;

import static io.metaloom.cortex.action.facedetect.FacedetectMedia.FACEDETECTION_RESULT_KEY;
import static io.metaloom.cortex.action.facedetect.FacedetectMedia.FACE_DETECTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.facedetect.FacedetectMedia;
import io.metaloom.cortex.action.facedetect.file.model.FaceData;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video.facedetect.face.impl.FaceImpl;

public class FacedetectMediaTest extends AbstractMediaTest {

	@Test
	public void testFaceCount() throws IOException {
		FacedetectMedia media = mediaVideo2().of(FACE_DETECTION);
		assertNull(media.getFaceCount());
		media.setFaceCount(42);
		assertEquals(42, media.getFaceCount());
		assertEquals(1, media.listXAttr().size());

		FacedetectMedia media2 = mediaVideo2().of(FACE_DETECTION);
		assertEquals(42, media2.getFaceCount());
	}

	@Test
	public void testFaceDetectionParameters() throws IOException {
		FacedetectMedia media = mediaVideo2().of(FACE_DETECTION);
		assertNull(media.getFacedetectionParams());
		assertNull(media.get(FACEDETECTION_RESULT_KEY));
		List<Face> entries = List.of(new FaceImpl());
		media.put(FACEDETECTION_RESULT_KEY, new FaceData(media.getSHA512()).setEntries(entries));
		assertNotNull(media.getFacedetectionParams());

		FacedetectMedia media2 = media(media.path()).of(FACE_DETECTION);
		assertNotNull(media2.get(FACEDETECTION_RESULT_KEY));
		FaceData params = media2.get(FACEDETECTION_RESULT_KEY);
		assertEquals(1, params.getEntries());

		// Now update the params
		Face face = new FaceImpl();
		params.getEntries().add(face);
		media2.setFacedetectionParams(params);

		FacedetectMedia media3 = media(media.path()).of(FACE_DETECTION);
		assertEquals(2, media3.getFacedetectionParams().getEntries());
	}

}
