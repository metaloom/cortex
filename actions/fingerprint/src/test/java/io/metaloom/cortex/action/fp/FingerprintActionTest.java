package io.metaloom.cortex.action.fp;

import static io.metaloom.cortex.api.action.media.action.FingerprintMedia.FINGERPRINT_KEY;
import static io.metaloom.cortex.api.action.media.action.HashMedia.SHA_512_KEY;
import static io.metaloom.loom.test.assertj.CortexAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.media.AbstractMediaTest;
import io.metaloom.cortex.action.media.LoomClientMock;
import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.data.TestDataCollection;

public class FingerprintActionTest extends AbstractMediaTest {

	@Test
	public void testAction() throws IOException {
		FingerprintAction action = mockAction();
		TestDataCollection data = TestEnvHelper.prepareTestdata("action-test");
		LoomMedia media = mediaVideo1();
		ActionResult2 result = action.process(ctx(media));
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512_KEY).hasXAttr(FINGERPRINT_KEY, data.sampleVideoFingerprint());
	}

	public FingerprintAction mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		return new FingerprintAction(client, new CortexOptions(), new FingerprintOptions());
	}
}
