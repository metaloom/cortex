package io.metaloom.cortex.common.meta;

import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;

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
import io.metaloom.utils.hash.SHA512;

/**
 * The meta storage implementation caches and delegates operation calls for keys on different key handlers.
 */
@Singleton
public class MetaStorageImpl implements MetaStorage {

	private final Set<LoomMetaTypeHandler> handlers;

	@Inject
	public MetaStorageImpl(Set<LoomMetaTypeHandler> handlers) {
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
	public <T> List<T> getAll(LoomMedia media, LoomMetaKey<T> metaKey) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");
		LoomMetaTypeHandler handler = getHandler(metaKey.type());
		return handler.getAll(media, metaKey);
	}

	@Override
	public <T> void append(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");
		LoomMetaTypeHandler handler = getHandler(metaKey.type());
		handler.append(media, metaKey, value);
	}

	@Override
	public <T> void put(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		Objects.requireNonNull(metaKey, "There was no meta attribute key provided.");
		LoomMetaTypeHandler handler = getHandler(metaKey.type());
		handler.put(media, metaKey, value);
	}

	@Override
	public void setSHA512(LoomMedia media, SHA512 hash) {
		put(media, SHA_512_KEY, hash);
	}

	@Override
	public SHA512 getSHA512(LoomMedia media) {
		return get(media, SHA_512_KEY);
	}

}
