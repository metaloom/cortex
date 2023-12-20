package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.media.hash.HashMedia.HASH;
import static io.metaloom.cortex.media.hash.HashMedia.MD5_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.cortex.media.hash.HashMedia;
import io.metaloom.utils.hash.MD5;
import io.metaloom.utils.hash.SHA512;

public class HashStorageTest extends AbstractMediaTest {

	@Test
	public void testSHA512() throws IOException {
		HashMedia media = mediaVideo1().of(HASH);
		assertNotNull(media.getSHA512(), "The sha512sum should always be computed");
		media.setSHA512(SHA512.fromString("abcd"));
		assertEquals("abcd", media.getSHA512());
		assertEquals(1, media.listXAttr().size());
		LoomMedia media2 = mediaVideo1();
		assertEquals("abcd", media2.getSHA512());
	}

	@Test
	public void testMD5() throws IOException {
		HashMedia media = mediaVideo1().of(HASH);
		assertNull(media.getMD5());
		media.put(MD5_KEY, MD5.fromString("abcd"));
		assertEquals("abcd", media.get(MD5_KEY));
		media.put(MD5_KEY, MD5.fromString("dcba"));
		assertEquals("dcba", media.get(MD5_KEY));
		assertEquals(1, media.listXAttr().size());
		assertEquals("dcba", media.getMD5());

		HashMedia media2 = mediaVideo1().of(HASH);
		assertEquals("dcba", media2.getMD5(), "We reload the media. The attribute should be read from xattr");
	}

}
