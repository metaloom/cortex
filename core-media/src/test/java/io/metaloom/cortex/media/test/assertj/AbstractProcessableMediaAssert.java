package io.metaloom.cortex.media.test.assertj;

import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;
import static io.metaloom.cortex.media.consistency.ConsistencyMedia.ZERO_CHUNK_COUNT_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.assertj.core.api.AbstractAssert;

import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.ProcessableMedia;
import io.metaloom.cortex.media.hash.HashMedia;
import io.metaloom.utils.hash.SHA512;

public abstract class AbstractProcessableMediaAssert<T extends AbstractProcessableMediaAssert<T, M>, M extends ProcessableMedia>
	extends AbstractAssert<T, M> {

	public AbstractProcessableMediaAssert(M actual, Class<?> clazz) {
		super(actual, clazz);
	}

	protected abstract T self();

	public T hasXAttr(LoomMetaKey<?> metaKey) {
		String fullKey = metaKey.fullKey();
		assertTrue(actual.listXAttr().contains(fullKey), "The attr " + fullKey + " was not found in the media file.");
		return self();
	}

	public T hasXAttr(LoomMetaKey<?>... keys) {
		for (LoomMetaKey<?> metaKey : keys) {
			assertEquals(XATTR, metaKey.type(),
				"The key is not enabled for persistance via xattr. It should thus not be present and must not be checked");
			String fullKey = metaKey.fullKey();
			assertTrue(actual.listXAttr().contains(fullKey), "The attr " + fullKey + " was not found in the media file.");
		}
		return self();
	}

	public T printXAttrKeys() {
		actual.listXAttr().forEach(System.out::println);
		return self();
	}

	public T hasXAttr(int count) {
		int actualCount = actual.listXAttr().size();
		if (count != actualCount) {
			String allKeys = String.join(" ,\n", actual.listXAttr());
			assertEquals(count, actualCount, "The count of xattr did not match the expected count. Got:\n" + allKeys);
		}
		return self();
	}
	
	public <R> T hasSHA512(SHA512 value) {
		assertEquals(value, actual.get(HashMedia.SHA_512_KEY), "The hash sum did not match.");
		return self();
	}

	public <R> T hasXAttr(LoomMetaKey<R> meta, R value) {
		R actualValue = actual.get(meta);
		assertNotNull(actualValue, "Did not find attribute value for " + meta);
		assertEquals(value, actualValue, "The value for " + meta + " did not match up.");
		return self();
	}

	public T hasXAttr(LoomMetaKey<String> meta, String value) {
		String actualValue = actual.get(meta);
		assertNotNull(actualValue, "Did not find attribute value for " + meta);
		assertEquals(value, actualValue, "The value for " + meta + " did not match up.");
		return self();
	}

	public T hasXAttr(LoomMetaKey<Long> meta, long value) {
		Long actualValue = actual.get(meta);
		assertNotNull(actualValue, "Did not find attribute value for " + meta);
		assertEquals(value, actualValue.longValue(), "The value for " + meta + " did not match up.");
		return self();
	}

	public T isConsistent() {
		hasXAttr(ZERO_CHUNK_COUNT_KEY, 0L);
		return self();
	}

}
