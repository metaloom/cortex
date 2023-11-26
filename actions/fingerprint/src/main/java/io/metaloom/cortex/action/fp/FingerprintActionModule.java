package io.metaloom.cortex.action.fp;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class FingerprintActionModule extends AbstractActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(FingerprintAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(FingerprintOptions.class, "fingerprint");
	}

	@Provides
	public static FingerprintOptions options(CortexOptions options) {
		return actionOptions(options, "fingerprint", new FingerprintOptions());
	}

}
