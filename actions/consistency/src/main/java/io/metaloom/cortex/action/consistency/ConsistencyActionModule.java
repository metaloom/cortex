package io.metaloom.cortex.action.consistency;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class ConsistencyActionModule extends AbstractActionModule {

	@Binds
	@IntoSet
	abstract FilesystemAction bindAction(ConsistencyAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(ConsistencyActionOptions.class, "consistency");
	}

	@Provides
	public static ConsistencyActionOptions options(CortexOptions options) {
		return actionOptions(options, "consistency", new ConsistencyActionOptions());
	}
}
