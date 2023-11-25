package io.metaloom.cortex.action.media.source;

import java.nio.file.Path;

import io.metaloom.cortex.action.common.media.impl.LoomMediaImpl;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.loom.test.data.TestDataCollection;

public interface MediaSource {

	TestDataCollection data();

	default LoomMedia media(Path path) {
		return new LoomMediaImpl(path);
	}

}
