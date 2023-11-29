package io.metaloom.cortex.api.media;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

public interface FingerprintMedia extends ProcessableMedia {

	public static final LoomMetaKey<String> FINGERPRINT_KEY = metaKey("fingerprint", 1, XATTR, String.class);

	default String getFingerprint() {
		return get(FINGERPRINT_KEY);
	}

	default FingerprintMedia setFingerprint(String fp) {
		put(FINGERPRINT_KEY, fp);
		return this;
	}

}
