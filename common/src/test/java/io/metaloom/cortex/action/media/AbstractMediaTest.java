package io.metaloom.cortex.action.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;

import io.metaloom.cortex.action.api.media.LoomMedia;
import io.metaloom.cortex.action.common.media.LoomMediaImpl;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.Testdata;

public abstract class AbstractMediaTest {

	protected Testdata data;

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
