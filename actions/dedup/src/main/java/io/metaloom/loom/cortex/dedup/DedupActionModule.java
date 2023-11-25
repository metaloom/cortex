package io.metaloom.loom.cortex.dedup;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public interface DedupActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(DedupAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(DedupOptions.class, "dedup");
	}

}