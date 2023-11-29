package io.metaloom.cortex.common.action.dummy;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;

public class DummyActionTest extends AbstractMediaTest {

	@Test
	public void testDummyAction() throws IOException {
		DummyAction action = new DummyAction(null, new CortexOptions(), new DummyOptions(), null);

		LoomMedia media = mock(LoomMedia.class);
		action.process(ctx(media));
		assertTrue(action.wasInvoked());
	}
}
