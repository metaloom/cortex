package io.metaloom.cortex.cli.dagger;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.impl.CortexImpl;

@Module
public abstract class CortexBindModule {

	@Binds
	@Singleton
	abstract Cortex cortex(CortexImpl e);

}
