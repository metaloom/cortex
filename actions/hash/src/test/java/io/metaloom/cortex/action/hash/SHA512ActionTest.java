package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.action.media.LoomMedia.*;
import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class SHA512ActionTest extends AbstractChunkActionTest<SHA512Action> {

	@Test
	public void testProcessing() throws IOException {
		LoomMedia media = sampleVideoMedia();
		ActionResult result = action().process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512_KEY, sampleVideoSHA512());
	}

	@Override
	public SHA512Action mockAction(LoomGRPCClient client) {
		return new SHA512Action(client, null, null);
	}
}
