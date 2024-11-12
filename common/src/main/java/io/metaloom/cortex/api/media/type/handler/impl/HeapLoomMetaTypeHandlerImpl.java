package io.metaloom.cortex.api.media.type.handler.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.type.LoomMetaCoreType;
import io.metaloom.cortex.api.media.type.LoomMetaType;
import io.metaloom.cortex.api.media.type.handler.AbstractCachingLoomTypeHandler;
import io.metaloom.cortex.api.meta.MetaDataStream;

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
	public <T> void put(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		checkAttrSupport(metaKey);
		super.put(media, metaKey, value);
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		checkAttrSupport(metaKey);
		return super.has(media, metaKey);
	}

	@Override
	public <T> T get(LoomMedia media, LoomMetaKey<T> metaKey) {
		checkAttrSupport(metaKey);
		return super.get(media, metaKey);
	}

	private <T> void checkAttrSupport(LoomMetaKey<T> metaKey) {
		if (metaKey.getValueClazz().isAssignableFrom(MetaDataStream.class)) {
			throw new MetaStorageException("Streams are not supported for XAttr");
		}
	}
}
