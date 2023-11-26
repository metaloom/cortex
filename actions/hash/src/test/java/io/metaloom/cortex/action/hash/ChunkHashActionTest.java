package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.action.media.action.ChunkMedia.CHUNK_HASH_KEY;
import static io.metaloom.loom.test.assertj.CortexAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.AbstractActionTest;
import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class ChunkHashActionTest extends AbstractActionTest<ChunkHashAction> {

	@Test
	public void testProcessing() throws IOException {
		LoomMedia media = mediaVideo1();
		ActionResult2 result = action().process(ctx(media));
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(CHUNK_HASH_KEY, sampleVideoChunkHash());

		ActionResult2 result2 = action().process(ctx(media));
		assertThat(result2).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(CHUNK_HASH_KEY, sampleVideoChunkHash());
	}

	@Override
	public ChunkHashAction mockAction(LoomGRPCClient client) {
		return new ChunkHashAction(client, null, null);
	}

}
