package io.metaloom.cortex.common.media;

import java.nio.file.Path;

import dagger.Module;
import dagger.Provides;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.media.impl.LoomMediaImpl;

@Module
public class LoomMediaModule {

	@Provides
	public LoomMedia media(Path path) {
		return new LoomMediaImpl(path);
	}

}
