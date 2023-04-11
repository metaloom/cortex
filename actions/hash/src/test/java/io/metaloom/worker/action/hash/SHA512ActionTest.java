package io.metaloom.worker.action.hash;

import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;
import static io.metaloom.worker.action.api.ProcessableMediaMeta.SHA_512;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;

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
