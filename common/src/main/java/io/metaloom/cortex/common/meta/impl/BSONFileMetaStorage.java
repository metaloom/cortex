package io.metaloom.cortex.common.meta.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.io.Files;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.param.BSONAttr;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.bson.BSON;
import io.metaloom.utils.fs.XAttrUtils;

@Singleton
public class BSONFileMetaStorage implements MetaStorage {

	private final CortexOptions options;

	private Map<String, Object> attrCache = new HashMap<>();

	@Inject
	public BSONFileMetaStorage(CortexOptions options) {
		this.options = options;
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		return toFile(media, metaKey).exists();
	}

	@Override
	public <T> T get(LoomMedia media, LoomMetaKey<T> metaKey) {
		switch (metaKey.type()) {
		case FS:
			try (FileInputStream fis = new FileInputStream(toFile(media, metaKey))) {
				return (T) BSON.readValue(fis, metaKey.getValueClazz());
			} catch (Exception e) {
				throw new RuntimeException("Error while reading value {" + metaKey.fullKey() + "}", e);
			}
		case HEAP:
			// TODO impl
			return null;
		case XATTR:
			return (T) XAttrUtils.readAttr(media.path(), metaKey.key(), metaKey.getValueClazz());
		default:
			throw new RuntimeException("Unknown type " + metaKey.type());
		}
	}

	@Override
	public <T> void put(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		// TODO move this into storage impl.

		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");
		String fullKey = metaKey.fullKey();
		switch (metaKey.type()) {
		case XATTR:
			writeXAttr(media, metaKey, value);
			attrCache.put(fullKey, value);
			break;
		case FS:
			writeLocalStorage(media, metaKey, value);
			attrCache.put(fullKey, value);
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
		File file = toFile(media, metaKey);
		File folder = file.getParentFile();
		if (!folder.mkdirs()) {
			throw new IOException("Unable to create meta storage location for path " + folder);
		}
		Files.touch(file);
		return new FileOutputStream(file);
	}

	private <T> File toFile(LoomMedia media, LoomMetaKey<T> key) {
		Path path = options.getMetaPath().resolve(key.key()).resolve(media.getSHA512() + ".meta");
		return path.toFile();
	}

	/*
	 * 
	 * private Path basePath = Paths.get("target/local-storage");
	 * 
	 * @Override
	 * 
	 * @SuppressWarnings("unchecked") public <T> T get(LoomMetaKey<T> key) { String fullKey = key.fullKey(); T cacheValue = (T) attrCache.get(fullKey); if
	 * (cacheValue != null) { return cacheValue; } else { switch (key.type()) { case XATTR: return readXAttr(key); case FS: return readLocalStorage(key); case
	 * HEAP: // We already tried the cache. Just return null return null; default: throw new RuntimeException("Invalid persistance type"); } } }
	 * 
	 * 
	 * 
	 * 
	 * try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) { BSON.writeValue(fos, value); } return this; } catch (IOException e) { throw new
	 * RuntimeException(e); } }
	 * 
	 * protected <T> T readLocalStorage(LoomMetaKey<T> metaKey) { try { Path filePath = toFilePath(basePath, getSHA512()); if (Files.exists(filePath)) { try
	 * (FileInputStream fis = new FileInputStream(filePath.toFile())) { return (T) BSON.readValue(fis, metaKey.getValueClazz()); } } else { return null; } }
	 * catch (IOException e) { throw new RuntimeException(e); } }
	 * 
	 * private static Path toFilePath(Path basePath, SHA512 hash) throws IOException { Path dirPath = HashUtils.segmentPath(basePath, hash); return
	 * dirPath.resolve(Paths.get(hash.toString() + "_bson.loom")); }
	 * 
	 */

//	protected <T> LoomMedia writeLocalStorage(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
//		try { Path dirPath = HashUtils.segmentPath(base	Path, getSHA512()); if(!Files.exists(dirPath)) { Files.createDirectories(dirPath); } Path filePath = toFilePath(basePath, getSHA512());
//	}

	protected <T> T readXAttr(LoomMedia media, LoomMetaKey<T> metaKey) {
		if (BSONAttr.class.isAssignableFrom(metaKey.getValueClazz())) {
			ByteBuffer bin = XAttrUtils.readBinAttr(media.path(), metaKey.fullKey());
			if (bin == null) {
				return null;
			}
			return (T) BSON.readValue(bin, metaKey.getValueClazz());
		} else {
			return (T) XAttrUtils.readAttr(media.path(), metaKey.fullKey(), metaKey.getValueClazz());
		}
	}

	protected <T> void writeXAttr(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		if (value instanceof BSONAttr) {
			byte[] data = BSON.writeValue(value);
			XAttrUtils.writeBinAttr(media.path(), metaKey.fullKey(), ByteBuffer.wrap(data));
		} else {
			XAttrUtils.writeAttr(media.path(), metaKey.fullKey(), value);
		}
	}
}
