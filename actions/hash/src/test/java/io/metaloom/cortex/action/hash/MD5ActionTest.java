package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.media.hash.HashMedia.MD5_KEY;
import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.media.test.AbstractBasicActionTest;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.test.data.TestMedia;

public class MD5ActionTest extends AbstractBasicActionTest<MD5Action> {

	@Override
	protected void assertProcessed(TestMedia testMedia, LoomMedia media, ActionResult result, MD5Action actionMock) {
		assertThat(media).hasXAttr(2).hasXAttr(MD5_KEY, testMedia.md5());
	}

	@Test
	@Override
	public void testProcessImage() throws IOException {
		super.testProcessImage();
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

}
