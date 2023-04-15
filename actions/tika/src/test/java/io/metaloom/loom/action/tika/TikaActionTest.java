package io.metaloom.loom.action.tika;

import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;
import static io.metaloom.cortex.action.api.ProcessableMediaMeta.SHA_512;
import static io.metaloom.cortex.action.api.ProcessableMediaMeta.TIKA_FLAGS;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.cortex.action.tika.TikaAction;
import io.metaloom.loom.cortex.action.tika.TikaActionSettings;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.Testdata;
import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.ProcessableMedia;
import io.metaloom.cortex.action.common.settings.ProcessorSettings;
import io.metaloom.cortex.action.media.AbstractWorkerTest;
import io.metaloom.cortex.action.media.LoomClientMock;

public class TikaActionTest extends AbstractWorkerTest {

	@Test
	public void testAction() throws IOException {
		TikaAction action = mockAction();
		Testdata data = TestEnvHelper.prepareTestdata("action-test");
		ProcessableMedia media = sampleVideoMedia(data);
		ActionResult result = action.process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512, TIKA_FLAGS);
	}

	public TikaAction mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		return new TikaAction(client, new ProcessorSettings(), new TikaActionSettings());
	}
}
