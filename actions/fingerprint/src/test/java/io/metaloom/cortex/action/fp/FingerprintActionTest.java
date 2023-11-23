package io.metaloom.cortex.action.fp;

import static io.metaloom.cortex.action.api.media.action.FingerprintMedia.FINGERPRINT_KEY;
import static io.metaloom.cortex.action.api.media.action.HashMedia.SHA_512_KEY;
import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.media.LoomMedia;
import io.metaloom.cortex.action.common.settings.ProcessorSettings;
import io.metaloom.cortex.action.media.AbstractMediaTest;
import io.metaloom.cortex.action.media.LoomClientMock;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.Testdata;

public class FingerprintActionTest extends AbstractMediaTest {

	@Test
	public void testAction() throws IOException {
		FingerprintAction action = mockAction();
		Testdata data = TestEnvHelper.prepareTestdata("action-test");
		LoomMedia media = sampleVideoMedia();
		ActionResult result = action.process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512_KEY).hasXAttr(FINGERPRINT_KEY, data.sampleVideoFingerprint());
	}

	public FingerprintAction mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		return new FingerprintAction(client, new ProcessorSettings(), new FingerprintActionSettings());
	}
}
