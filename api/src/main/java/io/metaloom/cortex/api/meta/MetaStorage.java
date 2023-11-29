package io.metaloom.cortex.api.meta;

import java.io.IOException;
import java.io.OutputStream;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.param.BSONAttr;

public interface MetaStorage {

	boolean has(LoomMedia media, MetaStorageKey key);

	OutputStream outputStream(LoomMedia media, MetaStorageKey key) throws IOException;

	<V extends BSONAttr> void write(LoomMedia media, MetaStorageKey key, V value);

	<T> void set(LoomMedia media, MetaStorageKey storageKey, T value);

}
