package io.metaloom.cortex.api.media;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

import io.metaloom.utils.hash.ChunkHash;

public interface ChunkMedia extends ProcessableMedia {

	public static final int VERSION = 1;

	public static final LoomMetaKey<Long> ZERO_CHUNK_COUNT_KEY = metaKey("zero_chunk_count", 1, XATTR, Long.class);

	public static final LoomMetaKey<ChunkHash> CHUNK_HASH_KEY = metaKey("chunk_hash", 1, XATTR, ChunkHash.class);

	default ChunkHash getChunkHash() {
		return get(CHUNK_HASH_KEY);
	}

	default ChunkMedia setChunkHash(ChunkHash chunkHash) {
		put(CHUNK_HASH_KEY, chunkHash);
		return this;
	}

	default Long getZeroChunkCount() {
		return get(ZERO_CHUNK_COUNT_KEY);
	}

	default ChunkMedia setZeroChunkCount(Long count) {
		put(ZERO_CHUNK_COUNT_KEY, count);
		return this;
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
