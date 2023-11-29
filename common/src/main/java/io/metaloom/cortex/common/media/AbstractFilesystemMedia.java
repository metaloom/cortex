package io.metaloom.cortex.common.media;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.param.BSONAttr;
import io.metaloom.cortex.common.bson.BSON;
import io.metaloom.utils.fs.XAttrUtils;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.SHA512;

public abstract class AbstractFilesystemMedia implements LoomMedia {

	private Path basePath = Paths.get("target/local-storage");

	protected <T> T readXAttr(LoomMetaKey<T> metaKey) {
		if (BSONAttr.class.isAssignableFrom(metaKey.getValueClazz())) {
			ByteBuffer bin = XAttrUtils.readBinAttr(path(), metaKey.fullKey());
			if (bin == null) {
				return null;
			}
			return (T) BSON.readValue(bin, metaKey.getValueClazz());
		} else {
			return (T) XAttrUtils.readAttr(path(), metaKey.fullKey(), metaKey.getValueClazz());
		}
	}

	protected <T> LoomMedia writeXAttr(LoomMetaKey<T> metaKey, T value) {
		if (value instanceof BSONAttr) {
			byte[] data = BSON.writeValue(value);
			XAttrUtils.writeBinAttr(path(), metaKey.fullKey(), ByteBuffer.wrap(data));
		} else {
			XAttrUtils.writeAttr(path(), metaKey.fullKey(), value);
		}
		return this;
	}

	protected <T> T readLocalStorage(LoomMetaKey<T> metaKey) {
		try {
			Path filePath = toFilePath(basePath, getSHA512());
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

	protected <T> LoomMedia writeLocalStorage(LoomMetaKey<T> metaKey, T value) {
		try {
			Path dirPath = HashUtils.segmentPath(basePath, getSHA512());
			if (!Files.exists(dirPath)) {
				Files.createDirectories(dirPath);
			}
			Path filePath = toFilePath(basePath, getSHA512());

			try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
				BSON.writeValue(fos, value);
			}
			return this;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static Path toFilePath(Path basePath, SHA512 hash) throws IOException {
		Path dirPath = HashUtils.segmentPath(basePath, hash);
		return dirPath.resolve(Paths.get(hash.toString() + "_bson.loom"));
	}

}
