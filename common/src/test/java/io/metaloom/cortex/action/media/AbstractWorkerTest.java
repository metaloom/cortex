package io.metaloom.cortex.action.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import io.metaloom.cortex.action.api.ProcessableMedia;
import io.metaloom.cortex.action.common.media.ProcessableMediaImpl;
import io.metaloom.loom.test.Testdata;

public abstract class AbstractWorkerTest {

	public static ProcessableMedia createTestMediaFile() throws IOException {
		File file = File.createTempFile("processable-media-test", "file");
		return new ProcessableMediaImpl(file.toPath());
	}

	public static ProcessableMedia sampleVideoMedia(Testdata data) {
		return new ProcessableMediaImpl(data.sampleVideoPath());
	}

	public static ProcessableMedia sampleVideoMedia2(Testdata data) {
		return new ProcessableMediaImpl(data.sampleVideo2Path());
	}

	public static ProcessableMedia sampleVideoMedia3(Testdata data) {
		return new ProcessableMediaImpl(data.sampleVideo3Path());
	}

	public static ProcessableMedia media(Path path) {
		return new ProcessableMediaImpl(path);
	}

}
