package io.metaloom.cortex.action.common.media.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.metaloom.cortex.action.common.media.AbstractFilesystemMedia;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.action.media.LoomMetaKey;
import io.metaloom.utils.fs.FilterHelper;
import io.metaloom.utils.fs.XAttrUtils;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.SHA512;

public class LoomMediaImpl extends AbstractFilesystemMedia {

	private Path path;

	private Map<String, Object> attrCache = new HashMap<>();

	public LoomMediaImpl(Path path) {
		this.path = path;
	}

	@Override
	public Path path() {
		return path;
	}

	@Override
	public long size() throws IOException {
		return Files.size(path);
	}

	@Override
	public boolean isVideo() {
		return FilterHelper.isVideo(path());
	}

	@Override
	public boolean isDocument() {
		return FilterHelper.isDocument(path());
	}

	@Override
	public boolean isImage() {
		return FilterHelper.isImage(path());
	}

	@Override
	public boolean isAudio() {
		return FilterHelper.isAudio(path());
	}

	@Override
	public boolean exists() {
		return file().exists();
	}

	@Override
	public File file() {
		return path.toFile();
	}

	@Override
	public String absolutePath() {
		return file().getAbsolutePath();
	}

	@Override
	public InputStream open() throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file()));
	}

	@Override
	public List<String> listXAttr() {
		return XAttrUtils.listAttr(path());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(LoomMetaKey<T> key) {
		String fullKey = key.fullKey();
		T cacheValue = (T) attrCache.get(fullKey);
		if (cacheValue != null) {
			return cacheValue;
		} else {
			switch (key.type()) {
			case XATTR:
				return readXAttr(key);
			case FS:
				return readLocalStorage(key);
			case HEAP:
				// We already tried the cache. Just return null
				return null;
			default:
				throw new RuntimeException("Invalid persistance type");
			}
		}
	}

	@Override
	public <T> LoomMedia put(LoomMetaKey<T> metaKey, T value) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");
		String fullKey = metaKey.fullKey();
		switch (metaKey.type()) {
		case XATTR:
			writeXAttr(metaKey, value);
			attrCache.put(fullKey, value);
			break;
		case FS:
			writeLocalStorage(metaKey, value);
			attrCache.put(fullKey, value);
			break;
		case HEAP:
			attrCache.put(fullKey, value);
			break;
		default:
			throw new RuntimeException("Invalid persistance type");
		}
		return this;
	}

	@Override
	public SHA512 getSHA512() {
		SHA512 hashSum512 = get(SHA_512_KEY);
		if (hashSum512 == null) {
			hashSum512 = SHA512.fromString(HashUtils.computeSHA512(file()));
			put(SHA_512_KEY, hashSum512);
		}
		return hashSum512;
	}

}
