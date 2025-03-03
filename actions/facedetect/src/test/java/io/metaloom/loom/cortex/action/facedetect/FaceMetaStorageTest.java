package io.metaloom.loom.cortex.action.facedetect;

import static io.metaloom.cortex.action.facedetect.FacedetectMedia.FACE_DETECTION;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.facedetect.FacedetectMedia;
import io.metaloom.loom.cortex.action.facedetect.avro.Facedetection;

public class FaceMetaStorageTest extends AbstractFacedetectMediaTest implements FaceDataTest {

	@Test
	public void testMetaStorageIntegration() {
		FacedetectMedia media = mediaVideo2().of(FACE_DETECTION);
		Facedetection data = createFaceData(media.getSHA512());
		storage().put(media, FacedetectMedia.FACEDETECTION_RESULT_KEY, data);
	}

}
