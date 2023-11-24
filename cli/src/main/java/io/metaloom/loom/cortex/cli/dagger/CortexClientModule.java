package io.metaloom.loom.cortex.cli.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.option.CortexOptionsLoader;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

@Module
public class CortexClientModule {

	@Provides
	@Singleton
	public LoomGRPCClient grpcClient(CortexOptions option) {
		return LoomGRPCClient.builder().build();
	}

	@Provides
	@Singleton
	public CortexOptions options(CortexOptionsLoader loader) {
		//System.out.println("O: " + actionOptions.size());
		CortexOptions options = loader.loadCortexOptions(loader.defaultConfigPath(), null);
		return options;
	}
}
