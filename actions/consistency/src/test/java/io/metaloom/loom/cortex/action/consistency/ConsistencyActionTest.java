package io.metaloom.loom.cortex.action.consistency;

import static io.metaloom.cortex.api.action.media.action.HashMedia.SHA_512_KEY;
import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.media.AbstractMediaTest;
import io.metaloom.cortex.action.media.LoomClientMock;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class ConsistencyActionTest extends AbstractMediaTest {

	@Test
	public void testSkipAction() throws IOException {
		ConsistencyAction action = mockAction();
		LoomMedia media = createTestMediaFile();
		ActionResult result = action.process(media);
		assertThat(result).isSkipped();
		assertThat(media).hasXAttr(SHA_512_KEY);
	}

	@Test
	public void testProcessVideo() throws IOException {
		ConsistencyAction action = mockAction();
		LoomMedia media = sampleVideoMedia();
		ActionResult result = action.process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(SHA_512_KEY, data.sampleVideoSHA512());
		assertThat(media).isConsistent();
		assertThat(media).hasXAttr(2);
	}

	private ConsistencyAction mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		ConsistencyAction action = new ConsistencyAction(client, null, null);
		return action;
	}
}
