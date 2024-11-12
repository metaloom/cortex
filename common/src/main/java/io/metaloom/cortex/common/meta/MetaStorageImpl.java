package io.metaloom.cortex.common.meta;

import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.type.LoomMetaCoreType;
import io.metaloom.cortex.api.media.type.LoomMetaType;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.api.media.type.handler.impl.MetaStorageException;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.utils.fs.FileUtils;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.SHA512;

/**
 * The meta storage implementation caches and delegates operation calls for keys on different key handlers.
 */
@Singleton
public class MetaStorageImpl implements MetaStorage {

	private final CortexOptions options;

	private final Set<LoomMetaTypeHandler> handlers;

	@Inject
	public MetaStorageImpl(CortexOptions options, Set<LoomMetaTypeHandler> handlers) {
		this.options = options;
		this.handlers = handlers;
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");
		LoomMetaCoreType type = metaKey.type();
		if (type == null) {
			Objects.requireNonNull(type, "Type not set");
		}
		return getHandler(type).has(media, metaKey);
	}

	private LoomMetaTypeHandler getHandler(LoomMetaCoreType type) {
		Optional<LoomMetaTypeHandler> op = handlers.stream().filter(h -> h.type() == type).findFirst();
		if (op.isEmpty()) {
			List<LoomMetaType> handlerTypes = handlers.stream().map(LoomMetaTypeHandler::type).collect(Collectors.toList());
			throw new MetaStorageException("Failed to locate handler for type " + type + ". Only know: " + handlerTypes);
		}
		return op.get();
	}

	@Override
	public <T> T get(LoomMedia media, LoomMetaKey<T> metaKey) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");
		LoomMetaTypeHandler handler = getHandler(metaKey.type());
		return handler.get(media, metaKey);
	}

	@Override
	public <T> void put(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");
		LoomMetaTypeHandler handler = getHandler(metaKey.type());
		handler.put(media, metaKey, value);
	}

//	/**
//	 * Use {@link MetaDataStream} in keys instead and access using {@link MetaDataStream#outputStream()}
//	 */
//	@Override
//	@Deprecated
//	public <T> OutputStream outputStream(LoomMedia media, LoomMetaKey<T> metaKey) throws IOException {
//		Path filePath = toMetaPath(media, metaKey);
//		FileUtils.ensureParentFolder(filePath);
//		com.google.common.io.Files.touch(filePath.toFile());
//		return new FileOutputStream(filePath.toFile());
//	}
//
//	/**
//	 * Use {@link MetaDataStream} in keys instead and access using {@link MetaDataStream#inputStream()}
//	 */
//	@Deprecated
//	@Override
//	public <T> InputStream inputStream(LoomMedia media, LoomMetaKey<T> metaKey) throws IOException {
//		Path filePath = toMetaPath(media, metaKey);
//		if (!Files.exists(filePath)) {
//			throw new IOException("Metadata file for " + media + " could not be found.");
//		}
//		return Files.newInputStream(filePath);
//	}

	@Override
	public void setSHA512(LoomMedia media, SHA512 hash) {
		put(media, SHA_512_KEY, hash);
	}

	@Override
	public SHA512 getSHA512(LoomMedia media) {
		return get(media, SHA_512_KEY);
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
