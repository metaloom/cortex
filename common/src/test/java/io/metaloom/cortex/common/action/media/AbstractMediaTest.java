package io.metaloom.cortex.common.action.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;

import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.media.impl.LoomMediaImpl;
import io.metaloom.cortex.common.meta.MetaStorageImpl;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.data.AudioData;
import io.metaloom.loom.test.data.DocData;
import io.metaloom.loom.test.data.ImageData;
import io.metaloom.loom.test.data.OtherData;
import io.metaloom.loom.test.data.TestDataCollection;
import io.metaloom.loom.test.data.TestMedia;
import io.metaloom.loom.test.data.TestMediaImpl.TestMediaBuilder;
import io.metaloom.loom.test.data.VideoData;
import io.metaloom.utils.hash.SHA512;

/**
 * Abstract class for tests which utilize test data files.
 */
public abstract class AbstractMediaTest implements DocData, ImageData, VideoData, AudioData, OtherData {

	private final static Path BASE_DIR = Paths.get("target/base-storage");

	protected TestDataCollection data;	

	public static final SHA512 SHA512_HASH = SHA512.fromString(
		"e7c22b994c59d9cf2b48e549b1e24666636045930d3da7c1acb299d1c3b7f931f94aae41edda2c2b207a36e10f8bcb8d45223e54878f5b316e7ce3b6bc019629");

	@BeforeEach
	public void setupData() throws IOException {
		data = TestEnvHelper.prepareTestdata("action-test");
	}

	protected CortexOptions options() {
		CortexOptions options = new CortexOptions();
		options.setMetaPath(BASE_DIR);
		return options;
	}

	public LoomMedia createEmptyLoomMedia() throws IOException {
		File file = File.createTempFile("processable-media-test", "file");
		return media(TestMediaBuilder.newBuilder(file.toPath()).build());
	}

	protected LoomMedia media(TestMedia media) {
		LoomMedia loomMedia = media(media.path());
//		if (loomMedia.exists()) {
//			loomMedia.setSHA512(media.sha512());
//		}
		return loomMedia;
	}

	protected LoomMedia media(Path path) {
		MetaStorage storage = new MetaStorageImpl(null);
		return new LoomMediaImpl(path, storage);
	}

	@Override
	public Path root() {
		return data.root();
	}

	public LoomMedia mediaImage1() {
		return media(image1());
	}

	public LoomMedia mediaVideo1() {
		return media(video1());
	}

	public LoomMedia mediaVideo2() {
		return media(video2());
	}

	public LoomMedia mediaVideo3() {
		return media(video3());
	}

	public LoomMedia mediaAudio1() {
		return media(audio1());
	}

	public LoomMedia mediaDocDOCX() {
		return media(docDOCX());
	}

	public LoomMedia mediaMissingMP4() {
		return media(missingMP4());
	}

	public LoomMedia mediaBogusBin() {
		return media(bogusBin());
	}

	public ActionContext ctx(LoomMedia media) {
		return ActionContext.create(media);
	}

}
