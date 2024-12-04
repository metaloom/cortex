package io.metaloom.cortex.common.media;

import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import io.metaloom.cortex.api.media.LoomMedia;

@Singleton
public class LoomMediaLoader {

	private Provider<LoomMediaComponent.Builder> loomMediaProvider;

	@Inject
	public LoomMediaLoader(Provider<LoomMediaComponent.Builder> loomMediaProvider) {
		this.loomMediaProvider = loomMediaProvider;
	}

	public LoomMedia load(Path path) {
		LoomMediaComponent component = loomMediaProvider.get().loomMediaPath(path).build();
		return component.media();
	}

}
