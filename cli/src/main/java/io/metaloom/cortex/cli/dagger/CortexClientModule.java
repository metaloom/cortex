package io.metaloom.cortex.cli.dagger;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.api.option.LoomOptions;
import io.metaloom.cortex.common.option.CortexOptionsLoader;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

@Module
public class CortexClientModule {

	public static final Logger log = LoggerFactory.getLogger(CortexClientModule.class);

	@Provides
	@Singleton
	@Nullable
	public LoomGRPCClient grpcClient(CortexOptions options) {
		LoomOptions loom = options.getLoom();
		if (loom == null) {
			log.error("Loom options not set. Unable to create gRPC client");
			return null;
		}

		String host = loom.getHostname();
		int port = loom.getPort();
		if (host == null) {
			log.error("No Loom host set. Switching to offline mode. Not creating gRPC client");
			return null;
		}
		return LoomGRPCClient.builder().setHostname(host).setPort(port).build();
	}

	@Provides
	@Singleton
	public CortexOptions options(@Nullable @Named("default-options") CortexOptions defaultOptions, CortexOptionsLoader loader) {
		if (defaultOptions != null) {
			return defaultOptions;
		} else {
			CortexOptions options = loader.load();
			return options;
		}
	}
}
