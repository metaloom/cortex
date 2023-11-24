package io.metaloom.loom.cortex.cli.dagger;

import javax.inject.Singleton;

import dagger.Component;
import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.option.CortexOptionsLoader;
import io.metaloom.loom.cortex.cli.CortexCLI;

@Singleton
@Component(modules = { CortexBindModule.class, LocalStorageModule.class, ActionModule.class, CortexClientModule.class })
public interface CortexComponent {

	Cortex cortex();

	CortexOptions options();

	CortexOptionsLoader optionsLoader();

	CortexCLI cli();

	@Component.Builder
	interface Builder {

		/**
		 * Build the component.
		 * 
		 * @return
		 */
		CortexComponent build();

	}

}
