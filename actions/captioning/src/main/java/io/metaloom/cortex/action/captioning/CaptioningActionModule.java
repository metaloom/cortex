package io.metaloom.cortex.action.captioning;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class CaptioningActionModule extends AbstractActionModule {

	@Binds
	@IntoSet
	abstract FilesystemAction bindAction(CaptioningAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(CaptioningActionOptions.class, "captioning");
	}

	@Provides
	public static CaptioningActionOptions options(CortexOptions options) {
		return actionOptions(options, "captioning", new CaptioningActionOptions());
	}
}
