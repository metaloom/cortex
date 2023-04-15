package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.action.api.ProcessableMediaMeta.SHA_512;
import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.ProcessableMedia;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class SHA512ActionTest extends AbstractChunkActionTest<SHA512Action> {

	@Test
	public void testProcessing() throws IOException {
		ProcessableMedia media = sampleVideoMedia();
		ActionResult result = action().process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512, sampleVideoSHA512());
	}

	@Override
	public SHA512Action mockAction(LoomGRPCClient client) {
		return new SHA512Action(client, null, null);
	}
}
