package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.action.api.ProcessableMediaMeta.CHUNK_HASH;
import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.ProcessableMedia;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class ChunkHashActionTest extends AbstractChunkActionTest<ChunkHashAction> {

	@Test
	public void testProcessing() throws IOException {
		ProcessableMedia media = sampleVideoMedia();
		ActionResult result = action().process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(CHUNK_HASH, sampleVideoChunkHash());
	}

	@Override
	public ChunkHashAction mockAction(LoomGRPCClient client) {
		return new ChunkHashAction(client, null, null);
	}

}
