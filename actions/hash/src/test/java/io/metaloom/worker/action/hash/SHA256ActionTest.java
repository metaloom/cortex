package io.metaloom.worker.action.hash;

import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;
import static io.metaloom.worker.action.api.ProcessableMediaMeta.SHA_256;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;

public class SHA256ActionTest extends AbstractChunkActionTest<SHA256Action> {

	@Test
	public void testProcessing() throws IOException {
		ProcessableMedia media = sampleVideoMedia();
		ActionResult result = action().process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_256, sampleVideoSHA256());
	}

	@Override
	public SHA256Action mockAction(LoomGRPCClient client) {
		return new SHA256Action(client, null, null);
	}

}
