package io.metaloom.loom.cortex.cli.dagger;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.metaloom.cortex.Cortex;
import io.metaloom.loom.cortex.cli.CortexCLI;
import io.metaloom.loom.cortex.impl.CortexImpl;

@Module
public abstract class CortexModule {

	@Binds
	@Singleton
	abstract Cortex cortex(CortexImpl e);

//	@Provides
//	@Singleton
//	public CortexCLI cli() {
//		return new CortexCLI();
//	}
}
