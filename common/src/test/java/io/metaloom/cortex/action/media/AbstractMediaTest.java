package io.metaloom.cortex.action.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;

import io.metaloom.cortex.action.common.media.impl.LoomMediaImpl;
import io.metaloom.cortex.action.media.source.AudioMediaSource;
import io.metaloom.cortex.action.media.source.DocMediaSource;
import io.metaloom.cortex.action.media.source.ImageMediaSource;
import io.metaloom.cortex.action.media.source.OtherMediaSource;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.data.TestDataCollection;
import io.metaloom.utils.hash.ChunkHash;
import io.metaloom.utils.hash.MD5;
import io.metaloom.utils.hash.SHA256;
import io.metaloom.utils.hash.SHA512;

/**
 * Abstract class for tests which utilize test data files.
 */
public abstract class AbstractMediaTest implements VideoMediaSource, DocMediaSource, AudioMediaSource, ImageMediaSource, OtherMediaSource {

	private final static Path BASE_DIR = Paths.get("target/base-storage");

	protected TestDataCollection data;

	public static final SHA512 HASH = SHA512.fromString(
		"e7c22b994c59d9cf2b48e549b1e24666636045930d3da7c1acb299d1c3b7f931f94aae41edda2c2b207a36e10f8bcb8d45223e54878f5b316e7ce3b6bc019629");

	@BeforeEach
	public void setupData() throws IOException {
		data = TestEnvHelper.prepareTestdata("action-test");
	}

	public LoomMedia createTestMediaFile() throws IOException {
		File file = File.createTempFile("processable-media-test", "file");
		return media(file.toPath());
	}

	
	@Override
	public TestDataCollection data() {
		return data;
	}

}
