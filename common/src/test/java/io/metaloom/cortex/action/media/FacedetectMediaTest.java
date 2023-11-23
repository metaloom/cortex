package io.metaloom.cortex.action.media;

import static io.metaloom.cortex.action.api.media.action.FacedetectionMedia.FACEDETECTION_PARAMS_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.api.media.LoomMedia;
import io.metaloom.cortex.action.api.media.param.FaceDetectionParameters;

public class FacedetectMediaTest extends AbstractMediaTest {

	@Test
	public void testFaceCount() throws IOException {
		LoomMedia media = sampleVideoMedia2();
		assertNull(media.getFaceCount());
		media.setFaceCount(42);
		assertEquals(42, media.getFaceCount());
		assertEquals(1, media.listXAttr().size());

		LoomMedia media2 = sampleVideoMedia2();
		assertEquals(42, media2.getFaceCount());
	}

	@Test
	public void testFaceDetectionParameters() throws IOException {
		LoomMedia media = sampleVideoMedia2();
		assertNull(media.getFacedetectionParams());
		assertNull(media.get(FACEDETECTION_PARAMS_KEY));
		media.put(FACEDETECTION_PARAMS_KEY, new FaceDetectionParameters().setCount(10));
		assertNotNull(media.getFacedetectionParams());

		LoomMedia media2 = media(media.path());
		assertNotNull(media2.get(FACEDETECTION_PARAMS_KEY));
		FaceDetectionParameters params = media2.get(FACEDETECTION_PARAMS_KEY);
		assertEquals(10, params.getCount());

		// Now update the params
		params.setCount(42);
		media2.setFacedetectionParams(params);

		LoomMedia media3 = media(media.path());
		assertEquals(42, media3.getFacedetectionParams().getCount());
	}

}
