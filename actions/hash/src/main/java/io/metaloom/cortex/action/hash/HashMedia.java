package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.action.hash.impl.HashMediaType;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.utils.hash.ChunkHash;
import io.metaloom.utils.hash.MD5;
import io.metaloom.utils.hash.SHA256;

public interface HashMedia extends LoomMedia {

	public static final HashMediaType HASH = new HashMediaType();

	public static final int VERSION = 1;

	public static final LoomMetaKey<ChunkHash> CHUNK_HASH_KEY = metaKey("chunk_hash", 1, XATTR, ChunkHash.class);

	public static final LoomMetaKey<SHA256> SHA_256_KEY = metaKey("sha256", 1, XATTR, SHA256.class);

	public static final LoomMetaKey<MD5> MD5_KEY = metaKey("md5", 1, XATTR, MD5.class);

	default ChunkHash getChunkHash() {
		return get(CHUNK_HASH_KEY);
	}

	default boolean hasChunkHash() {
		return getChunkHash() != null;
	}

	default void setChunkHash(ChunkHash chunkHash) {
		put(CHUNK_HASH_KEY, chunkHash);
	}

	default boolean hasSHA256() {
		return get(SHA_256_KEY) != null;
	}

	default SHA256 getSHA256() {
		return get(SHA_256_KEY);
	}

	default void setSHA256(SHA256 hash) {
		put(SHA_256_KEY, hash);
	}

	default MD5 getMD5() {
		return get(MD5_KEY);
	}

	default void setMD5(MD5 hashSum) {
		put(MD5_KEY, hashSum);
	}

	default boolean hasMD5() {
		return get(MD5_KEY) != null;
	}
}
