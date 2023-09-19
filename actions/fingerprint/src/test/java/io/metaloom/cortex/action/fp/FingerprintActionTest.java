package io.metaloom.cortex.action.fp;

import static io.metaloom.cortex.action.api.ProcessableMediaMeta.FINGERPRINT;
import static io.metaloom.cortex.action.api.ProcessableMediaMeta.SHA_512;
import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.ProcessableMedia;
import io.metaloom.cortex.action.common.settings.ProcessorSettings;
import io.metaloom.cortex.action.media.AbstractWorkerTest;
import io.metaloom.cortex.action.media.LoomClientMock;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.Testdata;

public class FingerprintActionTest extends AbstractWorkerTest {

	@Test
	public void testAction() throws IOException {
		FingerprintAction action = mockAction();
		Testdata data = TestEnvHelper.prepareTestdata("action-test");
		ProcessableMedia media = sampleVideoMedia(data);
		ActionResult result = action.process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512).hasXAttr(FINGERPRINT, data.sampleVideoFingerprint());
	}

	public FingerprintAction mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		return new FingerprintAction(client, new ProcessorSettings(), new FingerprintActionSettings());
	}
}
