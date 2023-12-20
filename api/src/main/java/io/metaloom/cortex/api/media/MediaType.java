package io.metaloom.cortex.api.media;

public interface MediaType<T extends LoomMedia> {

	T wrap(LoomMedia loomMedia);

}
