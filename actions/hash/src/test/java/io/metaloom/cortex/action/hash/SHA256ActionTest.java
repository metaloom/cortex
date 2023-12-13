package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.media.HashMedia.SHA_256_KEY;
import static io.metaloom.cortex.api.media.HashMedia.SHA_512_KEY;
import static io.metaloom.cortex.common.test.assertj.CortexAssertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractBasicActionTest;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.loom.test.data.TestMedia;

public class SHA256ActionTest extends AbstractBasicActionTest<SHA256Action> {

	@Test
	public void testProcessing() throws IOException {
		LoomMedia media = mediaVideo1();
		ActionResult result = action().process(ctx(media));
		assertThat(result).isSuccess();
		assertThat(media).hasXAttr(2)
			.hasXAttr(SHA_512_KEY)
			.hasXAttr(SHA_256_KEY, sampleVideoSHA256());
		assertThat(media).hasSHA256();
	}

	@Test
	@Override
	public void testDisabled() throws IOException {
		super.testDisabled();
	}

	@Test
	@Override
	public void testProcessDoc() throws IOException {
		super.testProcessDoc();
	}

	@Test
	public void testPullFromLoom() {
		// Mock the client
		AssetResponse response = mock(AssetResponse.class);
		LoomGRPCClient clientMock = mockClient(response);

		// Invoke the action
		LoomMedia media = mediaVideo1();
		ActionResult result = mockAction(clientMock).process(ctx(media));

		assertThat(result).isSuccess().isContinueNext();
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
		HashOptions options = mock(HashOptions.class);
		when(options.isSHA256()).thenReturn(true);
		return new SHA256Action(client, mock(CortexOptions.class), options);
	}

	@Override
	protected void assertProcessed(TestMedia testMedia, LoomMedia media, ActionResult result, SHA256Action actionMock) {
		assertThat(media).hasXAttr(2).hasXAttr(SHA_256_KEY, testMedia.sha256());
	}

	@Override
	protected void disableAction(SHA256Action actionMock) {
		HashOptions options = actionMock.options();
		when(options.isSHA256()).thenReturn(false);
	}

}
