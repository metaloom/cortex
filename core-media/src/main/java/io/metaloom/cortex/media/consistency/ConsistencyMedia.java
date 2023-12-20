package io.metaloom.cortex.media.consistency;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.action.consistency.impl.ConsistencyMediaType;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;

public interface ConsistencyMedia extends LoomMedia {

	public static final ConsistencyMediaType CONSISTENCY = new ConsistencyMediaType();

	public static final LoomMetaKey<Long> ZERO_CHUNK_COUNT_KEY = metaKey("zero_chunk_count", 1, XATTR, Long.class);

	default boolean hasZeroChunkCount() {
		Long count = getZeroChunkCount();
		return count != null;
	}

	default Long getZeroChunkCount() {
		return get(ZERO_CHUNK_COUNT_KEY);
	}

	default void setZeroChunkCount(Long count) {
		put(ZERO_CHUNK_COUNT_KEY, count);
	}

	default Boolean isComplete() {
		Long zeroChunks = get(ZERO_CHUNK_COUNT_KEY);
		if (zeroChunks != null) {
			return zeroChunks == 0;
		} else {
			return null;
		}
	}
}
