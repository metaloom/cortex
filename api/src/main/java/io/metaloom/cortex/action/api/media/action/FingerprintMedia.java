package io.metaloom.cortex.action.api.media.action;

import static io.metaloom.cortex.action.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.action.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.action.api.media.LoomMetaKey;
import io.metaloom.cortex.action.api.media.ProcessableMedia;

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
