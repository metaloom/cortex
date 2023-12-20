package io.metaloom.loom.cortex.action.consistency;

import static io.metaloom.cortex.media.consistency.ConsistencyMedia.CONSISTENCY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.cortex.media.consistency.ConsistencyMedia;

public class ConsistencyMediaTest extends AbstractMediaTest {

	@Test
	public void testGetSetUpdateLong() throws IOException {
		ConsistencyMedia media = createEmptyLoomMedia().of(CONSISTENCY);
		assertNull(media.getZeroChunkCount());
		media.setZeroChunkCount(42L);
		Long value1 = media.getZeroChunkCount();
		assertEquals(42L, value1.longValue());
		media.setZeroChunkCount(43L);
		Long value2 = media.getZeroChunkCount();
		assertEquals(43L, value2.longValue());
		assertEquals(1, media.listXAttr().size());
	}
}
