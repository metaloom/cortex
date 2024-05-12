package io.metaloom.cortex.common.meta;

import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.param.BSONAttr;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.bson.BSON;
import io.metaloom.utils.fs.XAttrUtils;
import io.metaloom.utils.hash.AbstractStringHash;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.SHA512;

@Singleton
public class MetaStorageImpl implements MetaStorage {

	private final CortexOptions options;

	// TODO inject cache and make it configurable
	private Cache<String, Object> attrCache = Caffeine.newBuilder()
		.maximumSize(10_000)
		.expireAfterWrite(Duration.ofDays(30))
		.build();

	@Inject
	public MetaStorageImpl(CortexOptions options) {
		this.options = options;
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");

		// We check the cache and return a result if the value has been cached.
		// Otherwise we will query the type specific implementation
		String fullKey = metaKey.fullKey();
		T cacheValue = (T) attrCache.getIfPresent(fullKey);
		if (cacheValue != null) {
			return true;
		}

		switch (metaKey.type()) {
		case FS:
			return toMetaPath(media, metaKey).toFile().exists();
		case HEAP:
			return attrCache.getIfPresent(metaKey.key()) != null;
		case XATTR:
			return XAttrUtils.hasXAttr(media.path(), metaKey.fullKey());
		default:
			throw new RuntimeException("Unable to check attribute " + metaKey.key() + " unknown type " + metaKey.type());
		}
	}

	@Override
	public <T> T get(LoomMedia media, LoomMetaKey<T> metaKey) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");

		String fullKey = metaKey.fullKey();
		T cacheValue = (T) attrCache.getIfPresent(fullKey);
		if (cacheValue != null) {
			return cacheValue;
		}

		switch (metaKey.type()) {
		case FS:
			return readLocalStorage(media, metaKey);
		case HEAP:
			// We already checked the cache before
			return null;
		case XATTR:
			return readXAttr(media, metaKey);
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
			break;
		case FS:
			try {
				writeLocalStorage(media, metaKey, value);
			} catch (IOException e) {
				throw new RuntimeException("Error while writing meta attribute to file", e);
			}
			break;
		case HEAP:
			// NOOP
			break;
		default:
			throw new RuntimeException("Invalid persistance type");
		}
		attrCache.put(fullKey, value);
	}

	@Override
	public <T> OutputStream outputStream(LoomMedia media, LoomMetaKey<T> metaKey) throws IOException {
		Path filePath = toMetaPath(media, metaKey);
		ensureParentFolder(filePath);
		com.google.common.io.Files.touch(filePath.toFile());
		return new FileOutputStream(filePath.toFile());
	}

	@Override
	public <T> InputStream inputStream(LoomMedia media, LoomMetaKey<T> metaKey) throws IOException {
		Path filePath = toMetaPath(media, metaKey);
		if (!Files.exists(filePath)) {
			throw new IOException("Metadata file for " + media + " could not be found.");
		}
		return Files.newInputStream(filePath);
	}

	@Override
	public void setSHA512(LoomMedia media, SHA512 hash) {
		put(media, SHA_512_KEY, hash);
	}

	@Override
	public SHA512 getSHA512(LoomMedia media) {
		return get(media, SHA_512_KEY);
	}

	protected <T> T readLocalStorage(LoomMedia media, LoomMetaKey<T> metaKey) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");
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
		if (AbstractStringHash.class.isAssignableFrom(metaKey.getValueClazz())) {
			ByteBuffer value = XAttrUtils.readBinXAttr(media.path(), metaKey.fullKey());
			if (value == null) {
				return null;
			}
			return metaKey.newValue(value);
		} else if (BSONAttr.class.isAssignableFrom(metaKey.getValueClazz())) {
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
