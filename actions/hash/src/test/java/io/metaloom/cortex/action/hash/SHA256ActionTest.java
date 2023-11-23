package io.metaloom.cortex.action.hash;

import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.media.LoomMedia;
import io.metaloom.cortex.action.api.media.action.HashMedia;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class SHA256ActionTest extends AbstractChunkActionTest<SHA256Action> {

	@Test
	public void testProcessing() throws IOException {
		LoomMedia media = sampleVideoMedia();
		ActionResult result = action().process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(HashMedia.SHA_256_KEY, sampleVideoSHA256());
	}

	@Override
	public SHA256Action mockAction(LoomGRPCClient client) {
		return new SHA256Action(client, null, null);
	}

}
