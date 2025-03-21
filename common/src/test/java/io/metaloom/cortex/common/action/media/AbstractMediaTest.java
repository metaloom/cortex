package io.metaloom.cortex.common.action.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.impl.LoomMetaKeyImpl;
import io.metaloom.cortex.api.media.type.LoomMetaCoreType;
import io.metaloom.cortex.api.media.type.handler.impl.HeapLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.meta.MetaDataStream;
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

	public static final LoomMetaKey<String> DUMMY_FS_STR_KEY = new LoomMetaKeyImpl<>("dummy_fs_str", 0, LoomMetaCoreType.FS, String.class);

	public static final LoomMetaKey<MetaDataStream> DUMMY_FS_BIN_KEY = new LoomMetaKeyImpl<>("dummy_fs_bin", 0, LoomMetaCoreType.FS,
		MetaDataStream.class);

	public static final LoomMetaKey<MetaDataStream> DUMMY_XATTR_BIN_KEY = new LoomMetaKeyImpl<>("dummy_xattr_bin", 0, LoomMetaCoreType.XATTR,
		MetaDataStream.class);

	public static final LoomMetaKey<String> DUMMY_XATTR_STR_KEY = new LoomMetaKeyImpl<>("dummy_xattr_str", 0, LoomMetaCoreType.XATTR, String.class);

	public static final LoomMetaKey<String> DUMMY_HEAP_STR_KEY = new LoomMetaKeyImpl<>("dummy_heap_str", 0, LoomMetaCoreType.HEAP, String.class);

	public static final LoomMetaKey<MetaDataStream> DUMMY_HEAP_BIN_KEY = new LoomMetaKeyImpl<>("dummy_heap_str", 0, LoomMetaCoreType.HEAP,
		MetaDataStream.class);

	protected TestDataCollection data;

	protected CortexOptions cortexOptions;

	private Path metaPath;

	public static final SHA512 SHA512_HASH = SHA512.fromString(
		"e7c22b994c59d9cf2b48e549b1e24666636045930d3da7c1acb299d1c3b7f931f94aae41edda2c2b207a36e10f8bcb8d45223e54878f5b316e7ce3b6bc019629");

	@BeforeEach
	public void setupData() throws IOException {
		data = TestEnvHelper.prepareTestdata("action-test");
		metaPath = Files.createTempDirectory(Paths.get("target"), "action_test");
		cortexOptions = new CortexOptions();
		cortexOptions.setMetaPath(metaPath);
	}

	@AfterEach
	public void cleanup() throws IOException {
		if (metaPath != null) {
			FileUtils.deleteDirectory(metaPath.toFile());
		}
		cortexOptions = null;
	}

	protected CortexOptions options() {
		return cortexOptions;
	}

	public LoomMedia createEmptyLoomMedia() throws IOException {
		File file = File.createTempFile("processable-media-test", "file");
		return media(TestMediaBuilder.newBuilder(file.toPath()).build());
	}

	protected LoomMedia media(TestMedia media) {
		return media(media.path());
	}

	protected LoomMedia media(Path path) {
		return new LoomMediaImpl(path, storage());
	}

	/**
	 * Tests may override this method in order to provide custom storage implementations.
	 * 
	 * @return
	 */
	public MetaStorage storage() {
		return new MetaStorageImpl(Set.of(new HeapLoomMetaTypeHandlerImpl()));
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
