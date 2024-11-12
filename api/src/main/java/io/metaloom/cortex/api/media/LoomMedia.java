package io.metaloom.cortex.api.media;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.type.LoomMetaCoreType.XATTR;

import io.metaloom.utils.hash.SHA512;

public interface LoomMedia extends ProcessableMedia {

	public static final LoomMetaKey<SHA512> SHA_512_KEY = metaKey("sha512", 1, XATTR, SHA512.class, b -> SHA512.fromBuffer(b));

	SHA512 getSHA512();

	void setSHA512(SHA512 hash);

	boolean hasSHA512();

	default String shortHash() {
		SHA512 hash = getSHA512();
		if (hash == null) {
			return null;
		} else {
			return hash.toString().substring(0, 8);
		}
	}

	default <T extends LoomMedia> T of(MediaType<T> type) {
		return type.wrap(this);
	}

}
