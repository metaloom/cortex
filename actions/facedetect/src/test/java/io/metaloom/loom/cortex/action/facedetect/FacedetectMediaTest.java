package io.metaloom.loom.cortex.action.facedetect;

import static io.metaloom.cortex.action.facedetect.FacedetectMedia.FACEDETECTION_RESULT_KEY;
import static io.metaloom.cortex.action.facedetect.FacedetectMedia.FACE_DETECTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.facedetect.FacedetectMedia;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.api.media.type.handler.impl.AvroLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.media.type.handler.impl.XAttrLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.common.meta.MetaStorageImpl;
import io.metaloom.loom.cortex.action.facedetect.avro.Facedetection;
import io.metaloom.loom.cortex.action.facedetect.avro.FacedetectionBox;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video.facedetect.face.impl.FaceImpl;

public class FacedetectMediaTest extends AbstractFacedetectMediaTest {

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
		assertNull(media.getFacedetections());
		assertNull(media.get(FACEDETECTION_RESULT_KEY));

		FacedetectionBox box = FacedetectionBox.newBuilder()
			.setStartX(0)
			.setStartY(0)
			.setHeight(10)
			.setWidth(10)
			.build();

		Facedetection facedetection = Facedetection.newBuilder()
			.setAssetHash(media.getSHA512().toString())
			.setFrame(10)
			.setBox(box)
			.build();
		media.put(FACEDETECTION_RESULT_KEY, facedetection);
		assertEquals(2, media.getFacedetections().size());

		FacedetectMedia media2 = media(media.path()).of(FACE_DETECTION);
		assertNotNull(media2.get(FACEDETECTION_RESULT_KEY));
		Facedetection params = media2.get(FACEDETECTION_RESULT_KEY);
		// assertEquals(1, params.getEntries());

		// Now update the params
		Face face = new FaceImpl();
		// params.getEntries().add(face);
		media2.appendFacedetection(params);

		FacedetectMedia media3 = media(media.path()).of(FACE_DETECTION);
		// assertEquals(2, media3.getFacedetectionParams().getEntries());
	}

	@Override
	public MetaStorage storage() {
		Set<LoomMetaTypeHandler> handlers = Set.of(new AvroLoomMetaTypeHandlerImpl(cortexOptions), new XAttrLoomMetaTypeHandlerImpl());
		MetaStorage storage = new MetaStorageImpl(handlers);
		return storage;
	}

}
