package io.metaloom.cortex.action.hash;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public interface HashActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindSHA512Action(SHA512Action action);

	@Binds
	@IntoSet
	abstract CortexAction bindSHA256Action(SHA256Action action);

	@Binds
	@IntoSet
	abstract CortexAction bindMD5Action(MD5Action action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(HashOptions.class, "hash");
	}

	@Provides
	public static HashOptions options(CortexOptions options) {
		return (HashOptions) options.getActions().get("hash");
	}
}
