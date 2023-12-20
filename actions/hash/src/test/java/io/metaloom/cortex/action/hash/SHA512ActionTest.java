package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;
import static io.metaloom.cortex.common.test.assertj.CortexAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractBasicActionTest;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.test.data.TestMedia;

public class SHA512ActionTest extends AbstractBasicActionTest<SHA512Action> {

	@Override
	protected void assertProcessed(TestMedia testMedia, LoomMedia media, ActionResult result, SHA512Action actionMock) {
		assertThat(media).hasXAttr(1).hasXAttr(SHA_512_KEY, testMedia.sha512());
	}

	@Test
	@Override
	public void testProcessAudio() throws IOException {
		super.testProcessAudio();
	}

	@Test
	@Override
	public void testDisabled() throws IOException {
		super.testDisabled();
	}

	@Override
	protected void assertDisabled(LoomMedia media, ActionResult result) {
		assertThat(media).hasXAttr(0);
	}

	@Override
	protected LoomMedia media(TestMedia media) {
		LoomMedia loomMedia = media(media.path());
		return loomMedia;
	}

	@Test
	@Override
	public void testProcessImage() throws IOException {
		super.testProcessImage();
	}

	@Test
	public void testProcessing() throws IOException {
		LoomMedia media = mediaVideo1();
		ActionResult result = action().process(media);
		assertThat(result).isSuccess();
		assertThat(media).hasXAttr(1).hasXAttr(SHA_512_KEY, sampleVideoSHA512());
	}

	@Test
	@Override
	public void testProcessVideo() throws IOException {
		super.testProcessVideo();
	}

	@Override
	public SHA512Action mockAction(LoomGRPCClient client) {
		HashOptions options = mock(HashOptions.class);
		when(options.isSHA512()).thenReturn(true);
		return new SHA512Action(client, mock(CortexOptions.class), options);
	}

	@Override
	protected void disableAction(SHA512Action actionMock) {
		HashOptions options = actionMock.options();
		when(options.isSHA512()).thenReturn(false);
	}
}
