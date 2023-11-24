package io.metaloom.loom.cortex.action.consistency;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public interface ConsistencyActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(ConsistencyAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(ConsistencyOptions.class, "consistency");
	}

}
