package io.metaloom.cortex.api.media.impl;

import java.nio.ByteBuffer;
import java.util.function.Function;

import io.metaloom.cortex.LoomWorker;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.LoomMetaType;

public class LoomMetaKeyImpl<T> implements LoomMetaKey<T> {

	private final String key;
	private final int version;
	private final Class<T> valueClazz;
	private final LoomMetaType type;
	private final Function<ByteBuffer, T> creator;

	public LoomMetaKeyImpl(String key, int version, LoomMetaType type, Class<T> valueClazz) {
		this(key, version, type, valueClazz, null);
	}

	public LoomMetaKeyImpl(String key, int version, LoomMetaType type, Class<T> valueClazz, Function<ByteBuffer, T> creator) {
		this.key = key;
		this.version = version;
		this.type = type;
		this.valueClazz = valueClazz;
		this.creator = creator;
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
	public T newValue(ByteBuffer b) {
		if (creator == null) {
			throw new RuntimeException("No value creator function has been specified for this meta key");
		}
		return creator.apply(b);
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
