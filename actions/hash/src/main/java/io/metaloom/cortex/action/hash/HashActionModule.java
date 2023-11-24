package io.metaloom.cortex.action.hash;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public interface HashActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(HashAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(HashOptions.class, "hash");
	}
}
