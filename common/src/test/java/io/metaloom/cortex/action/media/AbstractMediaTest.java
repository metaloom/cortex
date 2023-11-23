package io.metaloom.cortex.action.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;

import io.metaloom.cortex.action.common.media.impl.LoomMediaImpl;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.Testdata;
import io.metaloom.utils.hash.SHA512Sum;

public abstract class AbstractMediaTest {

	
	private final static Path BASE_DIR = Paths.get("target/base-storage");
	
	protected Testdata data;

	public static final SHA512Sum HASH = SHA512Sum.fromString(
		"e7c22b994c59d9cf2b48e549b1e24666636045930d3da7c1acb299d1c3b7f931f94aae41edda2c2b207a36e10f8bcb8d45223e54878f5b316e7ce3b6bc019629");

	@BeforeEach
	public void setupData() throws IOException {
		data = TestEnvHelper.prepareTestdata("action-test");
	}

	public LoomMedia createTestMediaFile() throws IOException {
		File file = File.createTempFile("processable-media-test", "file");
		return media(file.toPath());
	}

	public LoomMedia sampleVideoMedia() {
		return media(data.sampleVideoPath());
	}

	public LoomMedia sampleImageMedia1() {
		return media(data.sampleImage1Path());
	}

	public LoomMedia sampleVideoMedia2() {
		return media(data.sampleVideo2Path());
	}

	public LoomMedia sampleVideoMedia3() {
		return media(data.sampleVideo3Path());
	}

	public String sampleVideoChunkHash() {
		return data.sampleVideoChunkHash();
	}

	public String sampleVideoSHA512() {
		return data.sampleVideoSHA512();
	}

	public String sampleVideoSHA256() {
		return data.sampleVideoSHA256();
	}

	public LoomMedia media(Path path) {
		return new LoomMediaImpl(path);
	}

	public Testdata data() {
		return data;
	}

}
