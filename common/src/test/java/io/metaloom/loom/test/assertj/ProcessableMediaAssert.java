package io.metaloom.loom.test.assertj;

import static io.metaloom.cortex.action.api.ProcessableMediaMeta.ZERO_CHUNK_COUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.assertj.core.api.AbstractAssert;

import io.metaloom.cortex.action.api.ProcessableMedia;
import io.metaloom.cortex.action.api.ProcessableMediaMeta;

public class ProcessableMediaAssert extends AbstractAssert<ProcessableMediaAssert, ProcessableMedia> {

	protected ProcessableMediaAssert(ProcessableMedia actual) {
		super(actual, ProcessableMediaAssert.class);
	}

	public ProcessableMediaAssert hasXAttr(String key, int version) {
		String fullKey = ProcessableMediaMeta.fullKey(key, version);
		assertTrue(actual.listXAttr().contains(fullKey),"The attr " + fullKey + " was not found in the media file.");
		return this;
	}

	public ProcessableMediaAssert hasXAttr(ProcessableMediaMeta... metas) {
		for (ProcessableMediaMeta meta : metas) {
			assertTrue( meta.isPersisted(),"The key is not enabled for persistance via xattr. It should thus not be present and must not be checked");
			String fullKey = meta.key();
			assertTrue( actual.listXAttr().contains(fullKey),"The attr " + fullKey + " was not found in the media file.");
		}
		return this;
	}

	public ProcessableMediaAssert printXAttrKeys() {
		actual.listXAttr().forEach(System.out::println);
		return this;
	}

	public ProcessableMediaAssert hasXAttr(int count) {
		assertEquals(count, actual.listXAttr().size(), "The count of xattr did not match the expected count.");
		return this;
	}

	public ProcessableMediaAssert hasXAttr(ProcessableMediaMeta meta, String value) {
		String actualValue = actual.get(meta);
		assertNotNull(actualValue, "Did not find attribute value for " + meta);
		assertEquals(value, actualValue, "The value for " + meta + " did not match up.");
		return this;
	}

	public ProcessableMediaAssert hasXAttr(ProcessableMediaMeta meta, long value) {
		Long actualValue = actual.get(meta);
		assertNotNull(actualValue, "Did not find attribute value for " + meta);
		assertEquals(value, actualValue.longValue(), "The value for " + meta + " did not match up.");
		return this;
	}

	public ProcessableMediaAssert isConsistent() {
		hasXAttr(ZERO_CHUNK_COUNT, 0L);
		return this;
	}

}
