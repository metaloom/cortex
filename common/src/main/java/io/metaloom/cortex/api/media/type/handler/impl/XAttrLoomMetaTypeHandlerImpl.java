package io.metaloom.cortex.api.media.type.handler.impl;

import java.nio.ByteBuffer;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.param.BSONAttr;
import io.metaloom.cortex.api.media.type.LoomMetaCoreType;
import io.metaloom.cortex.api.media.type.LoomMetaType;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.common.bson.BSON;
import io.metaloom.utils.fs.XAttrUtils;
import io.metaloom.utils.hash.AbstractStringHash;

public class XAttrLoomMetaTypeHandlerImpl implements LoomMetaTypeHandler {

	@Override
	public String name() {
		return "xattr";
	}

	@Override
	public LoomMetaType type() {
		return LoomMetaCoreType.XATTR;
	}

	@Override
	public <T> void store(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		writeXAttr(media, metaKey, value);
	}

	@Override
	public <T> T read(LoomMedia media, LoomMetaKey<T> metaKey) {
		return readXAttr(media, metaKey);
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		return XAttrUtils.hasXAttr(media.path(), metaKey.fullKey());
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

}
