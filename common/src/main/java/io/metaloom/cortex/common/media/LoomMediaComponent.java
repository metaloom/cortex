package io.metaloom.cortex.common.media;

import java.nio.file.Path;

import javax.inject.Named;

import dagger.BindsInstance;
import dagger.Subcomponent;
import io.metaloom.cortex.api.media.LoomMedia;

@Subcomponent(modules = LoomMediaModule.class)
public interface LoomMediaComponent {

	LoomMedia media();

	@Subcomponent.Builder
	interface Builder {

		@BindsInstance
		Builder loomMediaPath(@Named("mediaPath") Path path);

		LoomMediaComponent build();
	}
}
