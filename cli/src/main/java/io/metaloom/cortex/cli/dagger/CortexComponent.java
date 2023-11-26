package io.metaloom.cortex.cli.dagger;

import javax.inject.Singleton;

import dagger.Component;
import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.option.CortexOptionsLoader;
import picocli.CommandLine;

@Singleton
@Component(modules = { CortexBindModule.class, PicoModule.class, LocalStorageModule.class, ActionCollectionModule.class, CortexClientModule.class })
public interface CortexComponent {

	Cortex cortex();

	CortexOptions options();

	CortexOptionsLoader optionsLoader();

	CommandLine cli();

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
