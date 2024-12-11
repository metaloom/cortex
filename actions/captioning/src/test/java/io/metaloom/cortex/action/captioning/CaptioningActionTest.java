package io.metaloom.cortex.action.captioning;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.media.test.AbstractBasicActionTest;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.test.data.TestMedia;

public class CaptioningActionTest extends AbstractBasicActionTest<CaptioningAction> {

	@Override
	protected void assertProcessed(TestMedia testMedia, LoomMedia media, ActionResult result, CaptioningAction actionMock) {
		assertTrue(media.has(CaptioningAction.CAPTION_META_KEY));
	}

	@Override
	protected void disableAction(CaptioningAction actionMock) {
		CaptioningActionOptions options = actionMock.options();
		options.setEnabled(false);
	}

	@Override
	public CaptioningAction mockAction(LoomClient client, CortexOptions cortexOptions) {
		CaptioningActionOptions options = new CaptioningActionOptions();
		options.setEnabled(true);
		return new CaptioningAction(null, cortexOptions, options);
	}

}
