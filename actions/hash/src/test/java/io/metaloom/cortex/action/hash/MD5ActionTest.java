package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.action.media.action.HashMedia.MD5_KEY;
import static io.metaloom.loom.test.assertj.CortexAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.AbstractBasicActionTest;
import io.metaloom.cortex.action.ActionTestcases;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class MD5ActionTest extends AbstractBasicActionTest<MD5Action> implements ActionTestcases {

	@Override
	protected void assertProcessed(LoomMedia media, ActionResult result, MD5Action actionMock) {
		assertThat(media).hasXAttr(2).hasXAttr(MD5_KEY, sampleVideoMD5());
	}

	@Override
	public MD5Action mockAction(LoomGRPCClient client) {
		HashOptions options = mock(HashOptions.class);
		when(options.isMD5()).thenReturn(true);
		return new MD5Action(client, mock(CortexOptions.class), options);
	}

	@Override
	protected void disableAction(MD5Action actionMock) {
		HashOptions options = actionMock.options();
		when(options.isMD5()).thenReturn(false);
	}

	@Test
	@Override
	public void testProcessAudio() throws IOException {
		super.testProcessAudio();
	}

}
