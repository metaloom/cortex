package io.metaloom.cortex.api.media.type.handler.impl;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.type.LoomMetaCoreType;
import io.metaloom.cortex.api.media.type.LoomMetaType;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;

@Singleton
public class AvroLoomMetaTypeHandlerImpl implements LoomMetaTypeHandler {

	@Inject
	public AvroLoomMetaTypeHandlerImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String name() {
		return "avro";
	}

	@Override
	public LoomMetaType type() {
		return LoomMetaCoreType.AVRO;
	}

	@Override
	public <T> void store(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T read(LoomMedia media, LoomMetaKey<T> metaKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		// TODO Auto-generated method stub
		return false;
	}

}
