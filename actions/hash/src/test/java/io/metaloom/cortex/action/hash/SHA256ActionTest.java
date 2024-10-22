package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;
import static io.metaloom.cortex.media.hash.HashMedia.SHA_256_KEY;
import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;
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
import io.metaloom.cortex.media.test.AbstractBasicActionTest;
import io.metaloom.loom.api.asset.AssetId;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.client.common.LoomClientException;
import io.metaloom.loom.rest.model.asset.AssetResponse;
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
	public void testPullFromLoom() throws LoomClientException {
		// Mock the client
		AssetResponse response = mock(AssetResponse.class);
		LoomClient clientMock = mockClient(response);
		CortexOptions cortexOptions = null;

		// Invoke the action
		LoomMedia media = mediaVideo1();
		ActionResult result = mockAction(clientMock, cortexOptions).process(ctx(media));

		assertThat(result).isSuccess().isContinueNext();
		assertThat(media).hasXAttr(2)
			.hasXAttr(SHA_512_KEY)
			.hasXAttr(SHA_256_KEY, sampleVideoSHA256());

		assertThat(media).hasSHA256();

		// Verify that the db object was accessed
		AssetId id = any();
		verify(clientMock, times(1)).loadAsset(id);
		verify(response, times(1)).getHashes().getSHA256();
	}

	@Override
	public SHA256Action mockAction(LoomClient client, CortexOptions cortexOptions) {
		HashOptions options = mock(HashOptions.class);
		when(options.isSHA256()).thenReturn(true);
		return new SHA256Action(client, cortexOptions, options);
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
