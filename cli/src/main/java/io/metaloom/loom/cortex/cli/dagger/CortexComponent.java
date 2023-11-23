package io.metaloom.loom.cortex.cli.dagger;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.loom.cortex.cli.CortexCLI;

@Singleton
@Component(modules = { CortexModule.class, LocalStorageModule.class })
public interface CortexComponent {

	Cortex cortex();

	CortexCLI cli();

	@Component.Builder
	interface Builder {

		/**
		 * Inject configuration options.
		 * 
		 * @param options
		 * @return
		 */
		@BindsInstance
		Builder configuration(CortexOptions options);

		/**
		 * Build the component.
		 * 
		 * @return
		 */
		CortexComponent build();

	}

}
