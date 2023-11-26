package io.metaloom.cortex.action.loom;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class LoomActionModule extends AbstractActionModule {

	@Binds
	@IntoSet
	abstract FilesystemAction bindAction(LoomAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(LoomActionOptions.class, "loom");
	}

	@Provides
	public static LoomActionOptions options(CortexOptions options) {
		return actionOptions(options, "loom", new LoomActionOptions());
	}
}
