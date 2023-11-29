package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.media.ChunkMedia.CHUNK_HASH_KEY;
import static io.metaloom.cortex.common.test.assertj.CortexAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.action.AbstractActionTest;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class ChunkHashActionTest extends AbstractActionTest<ChunkHashAction> {

	@Test
	public void testProcessing() throws IOException {
		LoomMedia media = mediaVideo1();
		ActionResult result = action().process(ctx(media));
		assertThat(result).isSuccess();
		assertThat(media).hasXAttr(2).hasXAttr(CHUNK_HASH_KEY, sampleVideoChunkHash());

		ActionResult result2 = action().process(ctx(media));
		assertThat(result2).isSuccess();
		assertThat(media).hasXAttr(2).hasXAttr(CHUNK_HASH_KEY, sampleVideoChunkHash());
	}

	@Override
	public ChunkHashAction mockAction(LoomGRPCClient client) {
		return new ChunkHashAction(client, null, null, null);
	}

}
