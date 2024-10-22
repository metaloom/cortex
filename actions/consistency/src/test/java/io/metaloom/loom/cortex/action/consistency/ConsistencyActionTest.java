package io.metaloom.loom.cortex.action.consistency;

import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;
import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.consistency.ConsistencyAction;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.cortex.common.action.media.LoomClientMock;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.client.common.LoomClientException;

public class ConsistencyActionTest extends AbstractMediaTest {

	@Test
	public void testSkipAction() throws IOException, LoomClientException {
		ConsistencyAction action = mockAction();
		LoomMedia media = createEmptyLoomMedia();
		ActionResult result = action.process(media);
		assertThat(result).isSkipped();
		assertThat(media).hasSHA512();
	}

	@Test
	public void testProcessVideo() throws IOException, LoomClientException {
		ConsistencyAction action = mockAction();
		LoomMedia media = mediaVideo1();
		ActionResult result = action.process(media);
		assertThat(result).isSuccess();
		assertThat(media).hasXAttr(SHA_512_KEY, data.sampleVideoSHA512());
		assertThat(media).isConsistent();
		assertThat(media).hasXAttr(2);
	}

	private ConsistencyAction mockAction() throws LoomClientException {
		LoomClient client = LoomClientMock.mockClient();
		ConsistencyAction action = new ConsistencyAction(client, null, null);
		return action;
	}
}
