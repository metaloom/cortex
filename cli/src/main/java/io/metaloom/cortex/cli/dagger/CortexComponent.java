package io.metaloom.cortex.cli.dagger;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.media.LoomMediaLoader;
import io.metaloom.cortex.common.option.CortexOptionsLoader;
import picocli.CommandLine;

@Singleton
@Component(modules = { CortexBindModule.class, CortexMediaModule.class, PicoCLIModule.class, LocalStorageModule.class, ActionCollectionModule.class,
	CortexClientModule.class })
public interface CortexComponent {

	Cortex cortex();

	LoomMediaLoader loader();

	CortexOptions options();

	CortexOptionsLoader optionsLoader();

	CommandLine cli();

	@Component.Builder
	interface Builder {

		/**
		 * Inject the cortex options.
		 * 
		 * @param options
		 * @return
		 */
		@BindsInstance
		Builder options(@Named("default-options") @Nullable CortexOptions options);

		/**
		 * Build the component.
		 * 
		 * @return
		 */
		CortexComponent build();

	}

}
