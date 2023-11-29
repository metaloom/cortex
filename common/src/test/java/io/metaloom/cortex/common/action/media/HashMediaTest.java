package io.metaloom.cortex.common.action.media;

import static io.metaloom.cortex.api.media.HashMedia.MD5_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.media.HashMedia;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.utils.hash.MD5;
import io.metaloom.utils.hash.SHA512;

public class HashMediaTest extends AbstractMediaTest {

	@Test
	public void testSHA512() throws IOException {
		LoomMedia media = mediaVideo1();
		assertNotNull(media.getSHA512(), "The sha512sum should always be computed");
		media.put(HashMedia.SHA_512_KEY, SHA512.fromString("abcd"));
		assertEquals("abcd", media.get(HashMedia.SHA_512_KEY));
		assertEquals(1, media.listXAttr().size());

		LoomMedia media2 = mediaVideo1();
		assertEquals("abcd", media2.getSHA512());
	}

	@Test
	public void testMD5() throws IOException {
		LoomMedia media = mediaVideo1();
		assertNull(media.getMD5());
		media.put(MD5_KEY, MD5.fromString("abcd"));
		assertEquals("abcd", media.get(MD5_KEY));
		media.put(MD5_KEY, MD5.fromString("dcba"));
		assertEquals("dcba", media.get(MD5_KEY));
		assertEquals(1, media.listXAttr().size());
		assertEquals("dcba", media.getMD5());

		LoomMedia media2 = mediaVideo1();
		assertEquals("dcba", media2.getMD5(), "We reload the media. The attribute should be read from xattr");
	}

}
