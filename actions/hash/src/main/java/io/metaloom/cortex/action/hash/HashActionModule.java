package io.metaloom.cortex.action.hash;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class HashActionModule extends AbstractActionModule {

	@Binds
	@IntoSet
	abstract FilesystemAction bindSHA512Action(SHA512Action action);

	@Binds
	@IntoSet
	abstract FilesystemAction bindSHA256Action(SHA256Action action);

	@Binds
	@IntoSet
	abstract FilesystemAction bindMD5Action(MD5Action action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(HashActionOptions.class, HashActionOptions.KEY);
	}

	@Provides
	public static HashActionOptions options(CortexOptions options) {
		return actionOptions(options, HashActionOptions.KEY, new HashActionOptions());
	}
}
