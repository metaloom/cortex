package io.metaloom.cortex.media.fingerprint;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.type.LoomMetaCoreType.XATTR;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.media.fingerprint.impl.FingerprintMediaType;

public interface FingerprintMedia extends LoomMedia {

	public static final FingerprintMediaType FINGERPRINT = new FingerprintMediaType();

	public static final LoomMetaKey<String> FINGERPRINT_KEY = metaKey("fingerprint", 1, XATTR, String.class);

	default String getFingerprint() {
		return get(FINGERPRINT_KEY);
	}

	default void setFingerprint(String fp) {
		put(FINGERPRINT_KEY, fp);
	}

	default boolean hasFingerprint() {
		return has(FINGERPRINT_KEY);
	}
}
