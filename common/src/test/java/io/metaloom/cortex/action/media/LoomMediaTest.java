package io.metaloom.cortex.action.media;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.media.LoomMedia;

public class LoomMediaTest extends AbstractMediaTest {

	@Test
	public void testBasics() throws IOException {
		LoomMedia media = createTestMediaFile();
		assertTrue(media.exists());
	}

}
