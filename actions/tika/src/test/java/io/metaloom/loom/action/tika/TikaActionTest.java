package io.metaloom.loom.action.tika;

import static io.metaloom.cortex.api.action.media.action.HashMedia.SHA_512_KEY;
import static io.metaloom.cortex.api.action.media.action.TikaMedia.TIKA_FLAGS_KEY;
import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.media.AbstractMediaTest;
import io.metaloom.cortex.action.media.LoomClientMock;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.ProcessorSettings;
import io.metaloom.cortex.api.option.action.ActionOptions;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.cortex.action.tika.TikaAction;

public class TikaActionTest extends AbstractMediaTest {

	@Test
	public void testAction() throws IOException {
		TikaAction action = mockAction();
		LoomMedia media = sampleVideoMedia();
		ActionResult result = action.process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512_KEY, TIKA_FLAGS_KEY);
	}

	public TikaAction mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		return new TikaAction(client, new ProcessorSettings(), new ActionOptions());
	}
}
