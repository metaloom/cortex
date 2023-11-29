package io.metaloom.cortex.common.meta.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.io.Files;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.param.BSONAttr;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.meta.MetaStorageKey;
import io.metaloom.cortex.api.option.CortexOptions;

@Singleton
public class BSONFileMetaStorage implements MetaStorage {

	private final CortexOptions options;

	@Inject
	public BSONFileMetaStorage(CortexOptions options) {
		this.options = options;
	}

	@Override
	public boolean has(LoomMedia media, MetaStorageKey key) {
		return toFile(media, key).exists();
	}

	@Override
	public OutputStream outputStream(LoomMedia media, MetaStorageKey key) throws IOException {
		File file = toFile(media, key);
		File folder = file.getParentFile();
		if (!folder.mkdirs()) {
			throw new IOException("Unable to create meta storage location for path " + folder);
		}
		Files.touch(file);
		return new FileOutputStream(file);
	}

	private File toFile(LoomMedia media, MetaStorageKey key) {
		Path path = options.getMetaPath().resolve(key.name()).resolve(media.getSHA512() + ".meta");
		return path.toFile();
	}

	@Override
	public <V extends BSONAttr> void write(LoomMedia media, MetaStorageKey key, V value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void set(LoomMedia media, MetaStorageKey storageKey, T value) {
		// TODO Auto-generated method stub
		
	}

}
