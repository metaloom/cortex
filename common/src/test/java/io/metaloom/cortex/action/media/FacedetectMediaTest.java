package io.metaloom.cortex.action.media;

import static io.metaloom.cortex.api.action.media.action.FacedetectionMedia.FACEDETECTION_PARAMS_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.action.media.param.FaceDetectionParameters;

public class FacedetectMediaTest extends AbstractMediaTest {

	@BeforeEach
	public void setup() throws IOException {
		//FileUtils.deleteDirectory(BASE_DIR.toFile());
	}

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

	@Test
	public void testStorage() {

	}

	@Test
	public void testWriteRead() throws IOException {
//		FaceData faceData = new FaceData();
//		faceData.getEntries().add(new FaceImpl().setLabel("blub"));
//		Path path = STORAGE.store(HASH, faceData);
//		assertTrue(Files.exists(path));
//
//		FaceData readData = STORAGE.load(HASH);
//		assertNotNull(readData);
//		assertEquals("blub", readData.getEntries().get(0).label());
	}

}
