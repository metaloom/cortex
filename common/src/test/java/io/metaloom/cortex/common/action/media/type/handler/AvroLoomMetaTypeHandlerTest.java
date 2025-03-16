package io.metaloom.cortex.common.action.media.type.handler;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.type.LoomMetaCoreType.AVRO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.api.media.type.handler.impl.AvroLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.cortex.common.action.media.avro.DummyElement;

public class AvroLoomMetaTypeHandlerTest extends AbstractMediaTest {

	public static final LoomMetaKey<DummyElement> DUMMY_ELEMENT_KEY = metaKey("dummy", 1, AVRO, DummyElement.class);

	@Test
	public void testHandler() throws IOException {
		LoomMedia media = mock(LoomMedia.class);
		when(media.getSHA512()).thenReturn(SHA512SUM);
		System.out.println(cortexOptions.getMetaPath());
		LoomMetaTypeHandler type = new AvroLoomMetaTypeHandlerImpl(cortexOptions);
		for (int i = 0; i < 3; i++) {
			DummyElement element = DummyElement.newBuilder().setAssetHash("bogus_" + i).build();
			type.append(media, DUMMY_ELEMENT_KEY, element);
		}
		List<DummyElement> list = type.getAll(media, DUMMY_ELEMENT_KEY);
		assertEquals(3, list.size());
		for (int i = 0; i < 3; i++) {
			assertEquals("bogus_" + i, list.get(i).getAssetHash().toString());
		}
	}
}
