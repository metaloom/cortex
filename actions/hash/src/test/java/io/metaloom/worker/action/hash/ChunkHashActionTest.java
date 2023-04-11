package io.metaloom.worker.action.hash;

import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;
import static io.metaloom.worker.action.api.ProcessableMediaMeta.CHUNK_HASH;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;

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
