package io.metaloom.cortex.common.action.media.source;

import java.nio.file.Path;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.common.media.impl.LoomMediaImpl;
import io.metaloom.loom.test.data.TestDataCollection;

public interface MediaSource {

	TestDataCollection data();

	default LoomMedia media(Path path) {
		return new LoomMediaImpl(path, null);
	}

}
