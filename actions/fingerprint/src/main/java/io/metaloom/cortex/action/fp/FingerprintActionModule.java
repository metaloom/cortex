package io.metaloom.cortex.action.fp;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public interface FingerprintActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(FingerprintAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(FingerprintOptions.class, "fingerprint");
	}

}
