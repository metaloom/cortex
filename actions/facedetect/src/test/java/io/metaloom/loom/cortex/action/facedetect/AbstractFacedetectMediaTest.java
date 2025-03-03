package io.metaloom.loom.cortex.action.facedetect;

import java.util.Set;

import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.api.media.type.handler.impl.AvroLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.media.type.handler.impl.XAttrLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.cortex.common.meta.MetaStorageImpl;

public class AbstractFacedetectMediaTest extends AbstractMediaTest {

	@Override
	public MetaStorage storage() {
		Set<LoomMetaTypeHandler> handlers = Set.of(new AvroLoomMetaTypeHandlerImpl(cortexOptions), new XAttrLoomMetaTypeHandlerImpl());
		MetaStorage storage = new MetaStorageImpl(handlers);
		return storage;
	}
}
