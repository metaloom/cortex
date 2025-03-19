package io.metaloom.cortex.api.meta;

import java.util.List;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;

public interface MetaStorageAccess {

	MetaStorage storage();

	LoomMedia self();

	default <T> T get(LoomMetaKey<T> metaKey) {
		return storage().get(self(), metaKey);
	}

	default <T> List<T> getAll(LoomMetaKey<T> metaKey) {
		return storage().getAll(self(), metaKey);
	}

	default <T> void put(LoomMetaKey<T> metaKey, T value) {
		storage().put(self(), metaKey, value);
	}

	default <T> boolean has(LoomMetaKey<T> metaKey) {
		return storage().has(self(), metaKey);
	}

	default <T> void append(LoomMetaKey<T> metaKey, T value) {
		storage().append(self(), metaKey, value);
	}

}
