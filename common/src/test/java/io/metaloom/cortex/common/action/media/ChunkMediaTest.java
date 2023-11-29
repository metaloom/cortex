package io.metaloom.cortex.common.action.media;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.media.ChunkMedia;
import io.metaloom.cortex.api.media.LoomMedia;

public class ChunkMediaTest extends AbstractMediaTest {

	

	@Test
	public void testGetSetUpdateLong() throws IOException {
		LoomMedia media = createTestMediaFile();
		assertNull(media.getZeroChunkCount());
		media.put(ChunkMedia.ZERO_CHUNK_COUNT_KEY, 42L);
		Long value1 = media.get(ChunkMedia.ZERO_CHUNK_COUNT_KEY);
		assertEquals(42L, value1.longValue());
		media.put(ChunkMedia.ZERO_CHUNK_COUNT_KEY, 43L);
		Long value2 = media.getZeroChunkCount();
		assertEquals(43L, value2.longValue());
		assertEquals(1, media.listXAttr().size());
	}
}
