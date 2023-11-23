package io.metaloom.loom.test.assertj;

import static io.metaloom.cortex.api.action.media.LoomMetaType.XATTR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.assertj.core.api.AbstractAssert;

import io.metaloom.cortex.api.action.media.LoomMetaKey;
import io.metaloom.cortex.api.action.media.ProcessableMedia;
import io.metaloom.cortex.api.action.media.action.ChunkMedia;

public class ProcessableMediaAssert extends AbstractAssert<ProcessableMediaAssert, ProcessableMedia> {

	protected ProcessableMediaAssert(ProcessableMedia actual) {
		super(actual, ProcessableMediaAssert.class);
	}

	public ProcessableMediaAssert hasXAttr(LoomMetaKey<?> metaKey) {
		String fullKey = metaKey.fullKey();
		assertTrue(actual.listXAttr().contains(fullKey), "The attr " + fullKey + " was not found in the media file.");
		return this;
	}

	public ProcessableMediaAssert hasXAttr(LoomMetaKey<?>... keys) {
		for (LoomMetaKey<?> metaKey : keys) {
			assertEquals(XATTR, metaKey.type(),
				"The key is not enabled for persistance via xattr. It should thus not be present and must not be checked");
			String fullKey = metaKey.fullKey();
			assertTrue(actual.listXAttr().contains(fullKey), "The attr " + fullKey + " was not found in the media file.");
		}
		return this;
	}

	public ProcessableMediaAssert printXAttrKeys() {
		actual.listXAttr().forEach(System.out::println);
		return this;
	}

	public ProcessableMediaAssert hasXAttr(int count) {
		int actualCount = actual.listXAttr().size();
		if (count != actualCount) {
			String allKeys = String.join(" ,\n", actual.listXAttr());
			assertEquals(count, actualCount, "The count of xattr did not match the expected count. Got:\n" + allKeys);
		}
		return this;
	}

	public <T> ProcessableMediaAssert hasXAttr(LoomMetaKey<T> meta, T value) {
		T actualValue = actual.get(meta);
		assertNotNull(actualValue, "Did not find attribute value for " + meta);
		assertEquals(value, actualValue, "The value for " + meta + " did not match up.");
		return this;
	}

	public ProcessableMediaAssert hasXAttr(LoomMetaKey<String> meta, String value) {
		String actualValue = actual.get(meta);
		assertNotNull(actualValue, "Did not find attribute value for " + meta);
		assertEquals(value, actualValue, "The value for " + meta + " did not match up.");
		return this;
	}

	public ProcessableMediaAssert hasXAttr(LoomMetaKey<Long> meta, long value) {
		Long actualValue = actual.get(meta);
		assertNotNull(actualValue, "Did not find attribute value for " + meta);
		assertEquals(value, actualValue.longValue(), "The value for " + meta + " did not match up.");
		return this;
	}

	public ProcessableMediaAssert isConsistent() {
		hasXAttr(ChunkMedia.ZERO_CHUNK_COUNT_KEY, 0L);
		return this;
	}

}
