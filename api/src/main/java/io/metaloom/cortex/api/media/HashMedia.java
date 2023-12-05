package io.metaloom.cortex.api.media;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

import io.metaloom.utils.hash.MD5;
import io.metaloom.utils.hash.SHA256;
import io.metaloom.utils.hash.SHA512;

public interface HashMedia extends ProcessableMedia {

	public static final int VERSION = 1;

	public static final LoomMetaKey<SHA256> SHA_256_KEY = metaKey("sha256", 1, XATTR, SHA256.class);

	public static final LoomMetaKey<SHA512> SHA_512_KEY = metaKey("sha512", 1, XATTR, SHA512.class);

	public static final LoomMetaKey<MD5> MD5_KEY = metaKey("md5", 1, XATTR, MD5.class);

	SHA512 getSHA512();

	default String shortHash() {
		return getSHA512().toString().substring(0, 8);
	}

	default HashMedia setSHA512(SHA512 hash) {
		put(SHA_512_KEY, hash);
		return this;
	}

	default SHA256 getSHA256() {
		return get(SHA_256_KEY);
	}

	default HashMedia setSHA256(SHA256 hash) {
		put(SHA_256_KEY, hash);
		return this;
	}

	default MD5 getMD5() {
		return get(MD5_KEY);
	}

	default HashMedia setMD5(MD5 hashSum) {
		put(MD5_KEY, hashSum);
		return this;
	}

}
