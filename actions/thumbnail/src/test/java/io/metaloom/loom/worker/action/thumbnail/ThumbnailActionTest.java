package io.metaloom.loom.worker.action.thumbnail;

import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;
import static io.metaloom.worker.action.api.ProcessableMediaMeta.SHA_512;
import static io.metaloom.worker.action.api.ProcessableMediaMeta.THUMBNAIL_FLAGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.Testdata;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;
import io.metaloom.worker.action.common.settings.ProcessorSettings;
import io.metaloom.worker.action.media.AbstractWorkerTest;
import io.metaloom.worker.action.media.LoomClientMock;

public class ThumbnailActionTest extends AbstractWorkerTest {

	private File thumbnailDir;

	@BeforeEach
	public void setup() throws IOException {
		thumbnailDir = new File("target/test-output");
		if (thumbnailDir.exists()) {
			FileUtils.deleteDirectory(thumbnailDir);
		}
		assertTrue(thumbnailDir.mkdirs(), "Unable to create thumbnail directory.");
	}

	@Test
	public void testAction() throws IOException {
		ThumbnailAction action = mockAction();
		Testdata data = TestEnvHelper.prepareTestdata("action-test");
		ProcessableMedia media = sampleVideoMedia3(data);
		ActionResult result = action.process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512).hasXAttr(THUMBNAIL_FLAGS);
		assertThat(new File(thumbnailDir, media.getHash512() + ".jpg")).exists();
	}

	public ThumbnailAction mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		return new ThumbnailAction(client, new ProcessorSettings(), new ThumbnailActionSettings().setThumbnailPath(thumbnailDir.getAbsolutePath()));
	}
}
