package io.metaloom.cortex.action.dummy;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.media.AbstractMediaTest;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;

public class DummyActionTest extends AbstractMediaTest {

	@Test
	public void testDummyAction() throws IOException {
		DummyAction action = new DummyAction(null, new CortexOptions(), new DummyOptions());

		LoomMedia media = mock(LoomMedia.class);
		action.process(ctx(media));
		assertTrue(action.wasInvoked());
	}
}
