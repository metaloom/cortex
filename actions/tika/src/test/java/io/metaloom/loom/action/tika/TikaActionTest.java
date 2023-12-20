package io.metaloom.loom.action.tika;

import static io.metaloom.cortex.action.tika.TikaMedia.TIKA_FLAGS_KEY;
import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;
import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.tika.TikaAction;
import io.metaloom.cortex.action.tika.TikaActionOptions;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.cortex.common.action.media.LoomClientMock;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class TikaActionTest extends AbstractMediaTest {

	@Test
	public void testAction() throws IOException {
		TikaAction action = mockAction();
		LoomMedia media = mediaVideo1();
		ActionResult result = action.process(media);
		assertThat(result).isSuccess();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512_KEY, TIKA_FLAGS_KEY);
	}

	public TikaAction mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		return new TikaAction(client, new CortexOptions(), new TikaActionOptions());
	}
}
