package io.metaloom.loom.cortex.action.tika;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public interface TikaActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(TikaAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(TikaOptions.class, "tika");
	}

}
