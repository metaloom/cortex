package io.metaloom.cortex.action.api.media.impl;

import io.metaloom.cortex.LoomWorker;
import io.metaloom.cortex.action.api.media.LoomMetaKey;
import io.metaloom.cortex.action.api.media.LoomMetaType;

public class LoomMetaKeyImpl<T> implements LoomMetaKey<T> {

	private final String key;
	private final int version;
	private final Class<T> valueClazz;
	private final LoomMetaType type;

	public LoomMetaKeyImpl(String key, int version, LoomMetaType type, Class<T> valueClazz) {
		this.key = key;
		this.version = version;
		this.type = type;
		this.valueClazz = valueClazz;
	}

	@Override
	public int version() {
		return version;
	}

	@Override
	public String key() {
		return key;
	}

	@Override
	public Class<?> getValueClazz() {
		return valueClazz;
	}

	@Override
	public LoomMetaType type() {
		return type;
	}

	@Override
	public String fullKey() {
		return LoomWorker.PREFIX + "_" + LoomWorker.VERSION + "_" + key() + "_v" + version();
	}

	@Override
	public String toString() {
		return key() + "_v" + version() + ":" + type.name();
	}

}
