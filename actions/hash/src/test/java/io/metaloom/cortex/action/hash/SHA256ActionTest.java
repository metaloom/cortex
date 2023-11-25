package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.action.media.action.HashMedia.SHA_256_KEY;
import static io.metaloom.cortex.api.action.media.action.HashMedia.SHA_512_KEY;
import static io.metaloom.loom.test.assertj.CortexAssertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.AbstractActionTest;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;

public class SHA256ActionTest extends AbstractActionTest<SHA256Action> {

	@Test
	public void testProcessing() throws IOException {
		LoomMedia media = sampleVideoMedia();
		ActionResult result = action().process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2)
			.hasXAttr(SHA_512_KEY)
			.hasXAttr(SHA_256_KEY, sampleVideoSHA256());
		assertThat(media).hasSHA256();
	}
	

	@Test
	public void testPullFromLoom() {
		// Mock the client
		AssetResponse response = mock(AssetResponse.class);
		LoomGRPCClient clientMock = mockClient(response);

		// Invoke the action
		LoomMedia media = sampleVideoMedia();
		ActionResult result = mockAction(clientMock).process(media);

		assertThat(result).isProcessed().isContinueNext();
		assertThat(media).hasXAttr(2)
			.hasXAttr(SHA_512_KEY)
			.hasXAttr(SHA_256_KEY, sampleVideoSHA256());
		
		assertThat(media).hasSHA256();

		// Verify that the db object was accessed
		verify(clientMock, times(1)).loadAsset(any());
		verify(response, times(1)).getSha256Sum();
	}


	@Override
	public SHA256Action mockAction(LoomGRPCClient client) {
		return new SHA256Action(client, new CortexOptions(), new HashOptions());
	}

}
