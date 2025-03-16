package io.metaloom.cortex.api.media.type.handler.impl;

import java.nio.ByteBuffer;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.type.LoomMetaCoreType;
import io.metaloom.cortex.api.media.type.LoomMetaType;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.api.meta.MetaDataStream;
import io.metaloom.utils.fs.XAttrUtils;
import io.metaloom.utils.hash.AbstractStringHash;
import io.vertx.core.json.JsonObject;

@Singleton
public class XAttrLoomMetaTypeHandlerImpl implements LoomMetaTypeHandler {

	@Inject
	public XAttrLoomMetaTypeHandlerImpl() {
	}

	@Override
	public LoomMetaType type() {
		return LoomMetaCoreType.XATTR;
	}

	@Override
	public <T> void put(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		checkAttrSupport(metaKey);
		writeXAttr(media, metaKey, value);
	}

	@Override
	public <T> T get(LoomMedia media, LoomMetaKey<T> metaKey) {
		checkAttrSupport(metaKey);
		return readXAttr(media, metaKey);
	}

	@Override
	public <T> List<T> getAll(LoomMedia media, LoomMetaKey<T> metaKey) {
		throw new RuntimeException("Not supported operation for XAttr storage");
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		checkAttrSupport(metaKey);
		return XAttrUtils.hasXAttr(media.path(), metaKey.fullKey());
	}

	@Override
	public <T> void append(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		throw new RuntimeException("Not supported operation for XAttr storage");
	}

	private <T> void checkAttrSupport(LoomMetaKey<T> metaKey) {
		if (metaKey.getValueClazz().isAssignableFrom(MetaDataStream.class)) {
			throw new MetaStorageException("Streams are not supported for XAttr");
		}
	}

	protected <T> T readXAttr(LoomMedia media, LoomMetaKey<T> metaKey) {
		if (AbstractStringHash.class.isAssignableFrom(metaKey.getValueClazz())) {
			ByteBuffer value = XAttrUtils.readBinXAttr(media.path(), metaKey.fullKey());
			if (value == null) {
				return null;
			}
			return metaKey.newValue(value);
		} else if (JsonObject.class.isAssignableFrom(metaKey.getValueClazz())) {
			String str = XAttrUtils.readXAttr(media.path(), metaKey.fullKey(), String.class);
			return (T) new JsonObject(str);
		} else {
			return (T) XAttrUtils.readXAttr(media.path(), metaKey.fullKey(), metaKey.getValueClazz());
		}
	}

	protected <T> void writeXAttr(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		XAttrUtils.writeXAttr(media.path(), metaKey.fullKey(), value);
	}

}
