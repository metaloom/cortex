package io.metaloom.loom.worker.action.consistency;

import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;
import static io.metaloom.worker.action.api.ProcessableMediaMeta.SHA_512;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.Testdata;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;
import io.metaloom.worker.action.media.AbstractWorkerTest;
import io.metaloom.worker.action.media.LoomClientMock;

public class ConsistencyActionTest extends AbstractWorkerTest {

	@Test
	public void testSkipAction() throws IOException {
		ConsistencyAction action = mockAction();
		ProcessableMedia media = createTestMediaFile();
		ActionResult result = action.process(media);
		assertThat(result).isSkipped();
		assertThat(media).hasXAttr(SHA_512);
	}

	@Test
	public void testProcessVideo() throws IOException {
		ConsistencyAction action = mockAction();
		Testdata data = TestEnvHelper.prepareTestdata("action-test");
		ProcessableMedia media = sampleVideoMedia(data);
		ActionResult result = action.process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(SHA_512, data.sampleVideoSHA512());
		assertThat(media).isConsistent();
		assertThat(media).hasXAttr(2);
	}

	private ConsistencyAction mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		ConsistencyAction action = new ConsistencyAction(client, null, null);
		return action;
	}
}
