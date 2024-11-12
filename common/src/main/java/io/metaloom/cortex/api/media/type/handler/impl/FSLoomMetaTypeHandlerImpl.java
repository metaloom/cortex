package io.metaloom.cortex.api.media.type.handler.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.type.LoomMetaCoreType;
import io.metaloom.cortex.api.media.type.LoomMetaType;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.api.meta.MetaDataStream;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.bson.BSON;
import io.metaloom.cortex.common.meta.MetaDataStreamFSImpl;
import io.metaloom.utils.fs.FileUtils;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.SHA512;

@Singleton
public class FSLoomMetaTypeHandlerImpl implements LoomMetaTypeHandler {

	private final CortexOptions options;

	@Inject
	public FSLoomMetaTypeHandlerImpl(CortexOptions options) {
		this.options = options;
	}

	@Override
	public LoomMetaType type() {
		return LoomMetaCoreType.FS;
	}

	@Override
	public String name() {
		return "fs";
	}

	@Override
	public <T> void store(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		try {
			writeLocalStorage(media, metaKey, value);
		} catch (IOException e) {
			throw new RuntimeException("Error while writing meta attribute to file", e);
		}
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		return toMetaPath(media, metaKey).toFile().exists();
	}

	@Override
	public <T> T read(LoomMedia media, LoomMetaKey<T> metaKey) {
		return readLocalStorage(media, metaKey);
	}

	protected <T> T readLocalStorage(LoomMedia media, LoomMetaKey<T> metaKey) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");
		try {
			Path filePath = toMetaPath(media, metaKey);
			// Dedicated handling for stream access
			if (metaKey.getValueClazz().isAssignableFrom(MetaDataStream.class)) {
				return (T) new MetaDataStreamFSImpl(filePath);
			} else {
				// Try BSON instead
				if (Files.exists(filePath)) {
					try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
						return (T) BSON.readValue(fis, metaKey.getValueClazz());
					}
				} else {
					return null;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected <T> void writeLocalStorage(LoomMedia media, LoomMetaKey<T> metaKey, T value) throws IOException {
		Path filePath = toMetaPath(media, metaKey);
		FileUtils.ensureParentFolder(filePath);
		try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
			BSON.writeValue(fos, value);
		} catch (IOException e) {
			throw new RuntimeException("Error while writing value " + metaKey.fullKey() + " for media " + media.getSHA512(), e);
		}
	}

	protected <T> Path toMetaPath(LoomMedia media, LoomMetaKey<T> key) {
		SHA512 hash = media.getSHA512();
		String fileName = hash + ".meta";
		Path basePath = options.getMetaPath().resolve(key.key());
		Path dirPath = HashUtils.segmentPath(basePath, hash);
		Path filePath = dirPath.resolve(fileName);
		return filePath;
	}

}
