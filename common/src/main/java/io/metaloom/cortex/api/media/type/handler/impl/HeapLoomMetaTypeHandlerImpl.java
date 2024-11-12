package io.metaloom.cortex.api.media.type.handler.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.type.LoomMetaCoreType;
import io.metaloom.cortex.api.media.type.LoomMetaType;
import io.metaloom.cortex.api.media.type.handler.AbstractCachingLoomTypeHandler;

@Singleton
public class HeapLoomMetaTypeHandlerImpl extends AbstractCachingLoomTypeHandler {


	@Inject
	public HeapLoomMetaTypeHandlerImpl() {
	}

	@Override
	public LoomMetaType type() {
		return LoomMetaCoreType.HEAP;
	}

	@Override
	public String name() {
		return "heap";
	}

	@Override
	public <T> void store(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		// NOOP
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		return getCache().getIfPresent(metaKey.key()) != null;
	}

	@Override
	public <T> T read(LoomMedia media, LoomMetaKey<T> metaKey) {
		// We already checked the cache before
		return null;
	}
}
