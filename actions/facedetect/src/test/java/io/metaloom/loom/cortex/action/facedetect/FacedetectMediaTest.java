package io.metaloom.loom.cortex.action.facedetect;

import static io.metaloom.cortex.media.facedetect.FacedetectMedia.FACEDETECTION_RESULT_KEY;
import static io.metaloom.cortex.media.facedetect.FacedetectMedia.FACE_DETECTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.cortex.media.facedetect.FaceDetectionResult;
import io.metaloom.cortex.media.facedetect.FacedetectMedia;

public class FacedetectMediaTest extends AbstractMediaTest {

	@Test
	public void testFaceCount() throws IOException {
		FacedetectMedia media = mediaVideo2().of(FACE_DETECTION);
		assertNull(media.getFaceCount());
		media.setFaceCount( 42);
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
		media.put(FACEDETECTION_RESULT_KEY, new FaceDetectionResult().setCount(10));
		assertNotNull(media.getFacedetectionParams());

		FacedetectMedia media2 = media(media.path()).of(FACE_DETECTION);
		assertNotNull(media2.get(FACEDETECTION_RESULT_KEY));
		FaceDetectionResult params = media2.get(FACEDETECTION_RESULT_KEY);
		assertEquals(10, params.getCount());

		// Now update the params
		params.setCount(42);
		media2.setFacedetectionParams( params);

		FacedetectMedia media3 = media(media.path()).of(FACE_DETECTION);
		assertEquals(42, media3.getFacedetectionParams().getCount());
	}

	@Test
	public void testStorage() {

	}

	@Test
	public void testWriteRead() throws IOException {
		// FaceData faceData = new FaceData();
		// faceData.getEntries().add(new FaceImpl().setLabel("blub"));
		// Path path = STORAGE.store(HASH, faceData);
		// assertTrue(Files.exists(path));
		//
		// FaceData readData = STORAGE.load(HASH);
		// assertNotNull(readData);
		// assertEquals("blub", readData.getEntries().get(0).label());
	}

}
