package io.metaloom.cortex.action.fp;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;

public interface FingerprintMedia extends LoomMedia {

	public static final FingerprintMediaType FINGERPRINT = new FingerprintMediaType();

	public static final LoomMetaKey<String> FINGERPRINT_KEY = metaKey("fingerprint", 1, XATTR, String.class);

	default String getFingerprint() {
		return get(FINGERPRINT_KEY);
	}

	default void setFingerprint( String fp) {
		put( FINGERPRINT_KEY, fp);
	}
}
