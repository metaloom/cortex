package io.metaloom.cortex.action.tika;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class TikaActionModule extends AbstractActionModule {

	private static final String KEY = "tika";

	@Binds
	@IntoSet
	abstract FilesystemAction bindAction(TikaAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(TikaActionOptions.class, TikaActionModule.KEY);
	}

	@Provides
	public static TikaActionOptions options(CortexOptions options) {
		return actionOptions(options, KEY, new TikaActionOptions());
	}
}
