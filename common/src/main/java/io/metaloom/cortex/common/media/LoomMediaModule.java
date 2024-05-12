package io.metaloom.cortex.common.media;

import java.nio.file.Path;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.media.impl.LoomMediaImpl;

@Module
public class LoomMediaModule {

	@Provides
	public LoomMedia media(@Named("mediaPath") Path path, MetaStorage storage) {
		return new LoomMediaImpl(path, storage);
	}

}
