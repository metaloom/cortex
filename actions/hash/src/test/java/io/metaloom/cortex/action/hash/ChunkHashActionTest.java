package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.action.api.media.action.ChunkMedia.CHUNK_HASH_KEY;
import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.media.LoomMedia;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class ChunkHashActionTest extends AbstractChunkActionTest<ChunkHashAction> {

	@Test
	public void testProcessing() throws IOException {
		LoomMedia media = sampleVideoMedia();
		ActionResult result = action().process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(CHUNK_HASH_KEY, sampleVideoChunkHash());
	}

	@Override
	public ChunkHashAction mockAction(LoomGRPCClient client) {
		return new ChunkHashAction(client, null, null);
	}

}
