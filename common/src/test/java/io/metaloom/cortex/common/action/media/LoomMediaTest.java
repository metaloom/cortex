package io.metaloom.cortex.common.action.media;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.media.LoomMedia;

public class LoomMediaTest extends AbstractMediaTest {

	@Test
	public void testBasics() throws IOException {
		LoomMedia media = createTestMediaFile();
		assertTrue(media.exists());
	}

}
