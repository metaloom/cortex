package io.metaloom.cortex.common.meta.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.param.BSONAttr;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.bson.BSON;
import io.metaloom.utils.fs.XAttrUtils;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.SHA512;

@Singleton
public class MetaStorageImpl implements MetaStorage {

	private final CortexOptions options;

	private Map<String, Object> attrCache = new HashMap<>();

	@Inject
	public MetaStorageImpl(CortexOptions options) {
		this.options = options;
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		switch (metaKey.type()) {
		case FS:
			return toMetaPath(media, metaKey).toFile().exists();
		case HEAP:
			return attrCache.containsKey(metaKey.key());
		case XATTR:
			return XAttrUtils.hasXAttr(media.path(), metaKey.fullKey());
		default:
			throw new RuntimeException("Unable to check attribute " + metaKey.key() + " unknown type " + metaKey.type());
		}
	}

	@Override
	public <T> T get(LoomMedia media, LoomMetaKey<T> metaKey) {

		// String fullKey = key.fullKey(); T cacheValue = (T) attrCache.get(fullKey);
		// if (cacheValue != null) { return cacheValue; }

		switch (metaKey.type()) {
		case FS:
			readLocalStorage(media, metaKey);
		case HEAP:
			return (T) attrCache.get(metaKey.key());
		case XATTR:
			readXAttr(media, metaKey);
		default:
			throw new RuntimeException("Unknown type " + metaKey.type());
		}
	}

	@Override
	public <T> void put(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");
		String fullKey = metaKey.fullKey();
		switch (metaKey.type()) {
		case XATTR:
			writeXAttr(media, metaKey, value);
			attrCache.put(fullKey, value);
			break;
		case FS:
			try {
				writeLocalStorage(media, metaKey, value);
				attrCache.put(fullKey, value);
			} catch (IOException e) {
				throw new RuntimeException("Error while writing meta attribute to file", e);
			}
			break;
		case HEAP:
			attrCache.put(fullKey, value);
			break;
		default:
			throw new RuntimeException("Invalid persistance type");
		}
	}

	@Override
	public <T> OutputStream outputStream(LoomMedia media, LoomMetaKey<T> metaKey) throws IOException {
		Path filePath = toMetaPath(media, metaKey);
		ensureParentFolder(filePath);
		com.google.common.io.Files.touch(filePath.toFile());
		return new FileOutputStream(filePath.toFile());
	}

	protected <T> T readLocalStorage(LoomMedia media, LoomMetaKey<T> metaKey) {
		try {
			Path filePath = toMetaPath(media, metaKey);
			if (Files.exists(filePath)) {
				try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
					return (T) BSON.readValue(fis, metaKey.getValueClazz());
				}
			} else {
				return null;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected <T> void writeLocalStorage(LoomMedia media, LoomMetaKey<T> metaKey, T value) throws IOException {
		Path filePath = toMetaPath(media, metaKey);
		ensureParentFolder(filePath);
		try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
			BSON.writeValue(fos, value);
		} catch (IOException e) {
			throw new RuntimeException("Error while writing value " + metaKey.fullKey() + " for media " + media.getSHA512(), e);
		}
	}

	protected <T> T readXAttr(LoomMedia media, LoomMetaKey<T> metaKey) {
		if (BSONAttr.class.isAssignableFrom(metaKey.getValueClazz())) {
			ByteBuffer bin = XAttrUtils.readBinXAttr(media.path(), metaKey.fullKey());
			if (bin == null) {
				return null;
			}
			return (T) BSON.readValue(bin, metaKey.getValueClazz());
		} else {
			return (T) XAttrUtils.readXAttr(media.path(), metaKey.fullKey(), metaKey.getValueClazz());
		}
	}

	protected <T> void writeXAttr(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		if (value instanceof BSONAttr) {
			byte[] data = BSON.writeValue(value);
			XAttrUtils.writeBinXAttr(media.path(), metaKey.fullKey(), ByteBuffer.wrap(data));
		} else {
			XAttrUtils.writeXAttr(media.path(), metaKey.fullKey(), value);
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

	private static void ensureParentFolder(Path filePath) throws IOException {
		Path folderPath = filePath.getParent();
		if (!Files.exists(folderPath)) {
			Files.createDirectories(folderPath);
			// throw new IOException("Unable to create meta storage location for path " + folderPath);
		}
	}
}
