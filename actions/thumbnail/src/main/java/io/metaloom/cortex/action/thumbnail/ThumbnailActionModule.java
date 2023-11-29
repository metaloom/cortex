package io.metaloom.cortex.action.thumbnail;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class ThumbnailActionModule extends AbstractActionModule {

	@Binds
	@IntoSet
	abstract FilesystemAction<?> bindAction(ThumbnailAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(ThumbnailActionOptions.class, "thumbnail");
	}

	@Provides
	public static ThumbnailActionOptions options(CortexOptions options) {
		return actionOptions(options, "thumbnail", new ThumbnailActionOptions());
	}

}
