package io.metaloom.cortex.action.api.media.action;

import static io.metaloom.cortex.action.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.action.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.action.api.media.LoomMetaKey;
import io.metaloom.cortex.action.api.media.ProcessableMedia;

public interface HashMedia extends ProcessableMedia {

	public static final int VERSION = 1;

	public static final LoomMetaKey<String> SHA_256_KEY = metaKey("sha256", 1, XATTR, String.class);

	public static final LoomMetaKey<String> SHA_512_KEY = metaKey("sha512", 1, XATTR, String.class);

	public static final LoomMetaKey<String> MD5_KEY = metaKey("md5", 1, XATTR, String.class);

	String getSHA512();

	default HashMedia setSHA512(String hash) {
		put(SHA_512_KEY, hash);
		return this;
	}

	default String getSHA256() {
		return get(SHA_256_KEY);
	}

	default HashMedia setSHA256(String hash) {
		put(SHA_256_KEY, hash);
		return this;
	}

	default String getMD5() {
		return get(MD5_KEY);
	}

	default HashMedia setMD5(String hashSum) {
		put(MD5_KEY, hashSum);
		return this;
	}

}
