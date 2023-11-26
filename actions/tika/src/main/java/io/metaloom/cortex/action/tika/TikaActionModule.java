package io.metaloom.cortex.action.tika;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class TikaActionModule extends AbstractActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(TikaAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(TikaActionOptions.class, "tika");
	}

	@Provides
	public static TikaActionOptions options(CortexOptions options) {
		return actionOptions(options, "tika", new TikaActionOptions());
	}
}
