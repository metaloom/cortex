package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.media.hash.HashMedia.CHUNK_HASH_KEY;
import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.media.test.AbstractBasicActionTest;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.test.data.TestMedia;

public class ChunkHashActionTest extends AbstractBasicActionTest<ChunkHashAction> {

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
	public ChunkHashAction mockAction(LoomGRPCClient client, CortexOptions cortexOptions) {
		HashOptions options = mock(HashOptions.class);
		when(options.isMD5()).thenReturn(true);
		return new ChunkHashAction(client, cortexOptions, options);
	}

	@Override
	protected void assertProcessed(TestMedia testMedia, LoomMedia media, ActionResult result, ChunkHashAction actionMock) {
		assertThat(media).hasXAttr(2).hasXAttr(CHUNK_HASH_KEY, testMedia.chunkHash());
	}

	@Override
	protected void disableAction(ChunkHashAction actionMock) {
		HashOptions options = actionMock.options();
		when(options.isChunkHash()).thenReturn(false);
	}

}
