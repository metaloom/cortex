package io.metaloom.cortex.action.thumbnail;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public interface ThumbnailActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(ThumbnailAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(ThumbnailActionOptions.class, "thumbnail");
	}

}
