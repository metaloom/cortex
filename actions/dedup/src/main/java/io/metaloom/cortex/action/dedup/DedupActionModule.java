package io.metaloom.cortex.action.dedup;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class DedupActionModule extends AbstractActionModule {

	@Binds
	@IntoSet
	abstract FilesystemAction bindHashDedupAction(HashDedupAction action);

	@Binds
	@IntoSet
	abstract FilesystemAction bindFingerprintDedupAction(FingerprintDedupAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(DedupActionOptions.class, "dedup");
	}

	@Provides
	public static DedupActionOptions options(CortexOptions options) {
		return actionOptions(options, "dedup", new DedupActionOptions());
	}

}
