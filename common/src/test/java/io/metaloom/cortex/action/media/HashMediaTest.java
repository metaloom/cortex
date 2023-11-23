package io.metaloom.cortex.action.media;

import static io.metaloom.cortex.api.action.media.action.HashMedia.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.action.media.action.HashMedia;

public class HashMediaTest extends AbstractMediaTest {

	@Test
	public void testSHA512() throws IOException {
		LoomMedia media = sampleVideoMedia();
		assertNotNull(media.getSHA512(), "The sha512sum should always be computed");
		media.put(HashMedia.SHA_512_KEY, "abcd");
		assertEquals("abcd", media.get(HashMedia.SHA_512_KEY));
		assertEquals(1, media.listXAttr().size());

		LoomMedia media2 = sampleVideoMedia();
		assertEquals("abcd", media2.getSHA512());
	}

	@Test
	public void testMD5() throws IOException {
		LoomMedia media = sampleVideoMedia();
		assertNull(media.getMD5());
		media.put(MD5_KEY, "abcd");
		assertEquals("abcd", media.get(MD5_KEY));
		media.put(MD5_KEY, "dcba");
		assertEquals("dcba", media.get(MD5_KEY));
		assertEquals(1, media.listXAttr().size());
		assertEquals("dcba", media.getMD5());

		LoomMedia media2 = sampleVideoMedia();
		assertEquals("dcba", media2.getMD5(), "We reload the media. The attribute should be read from xattr");
	}

}
