package io.metaloom.loom.cortex.action.facedetect;

import static io.metaloom.cortex.action.facedetect.FacedetectMedia.FACE_DETECTION;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.facedetect.FacedetectMedia;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.api.media.type.handler.impl.AvroLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.media.type.handler.impl.XAttrLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.cortex.common.meta.MetaStorageImpl;
import io.metaloom.loom.cortex.action.facedetect.avro.Facedetection;

public class FaceMetaStorageTest extends AbstractMediaTest implements FaceDataTest {

	@Test
	public void testMetaStorageIntegration() {
		FacedetectMedia media = mediaVideo2().of(FACE_DETECTION);
		Facedetection data = createFaceData(media.getSHA512());
		storage().put(media, FacedetectMedia.FACEDETECTION_RESULT_KEY, data);
	}

	@Override
	public MetaStorage storage() {
		Set<LoomMetaTypeHandler> handlers = Set.of(new AvroLoomMetaTypeHandlerImpl(cortexOptions), new XAttrLoomMetaTypeHandlerImpl());
		MetaStorage storage = new MetaStorageImpl(handlers);
		return storage;
	}

}
