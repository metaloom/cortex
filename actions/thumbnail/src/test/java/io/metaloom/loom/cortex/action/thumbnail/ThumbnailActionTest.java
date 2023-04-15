package io.metaloom.loom.cortex.action.thumbnail;

import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;
import static io.metaloom.cortex.action.api.ProcessableMediaMeta.SHA_512;
import static io.metaloom.cortex.action.api.ProcessableMediaMeta.THUMBNAIL_FLAGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.ProcessableMedia;
import io.metaloom.cortex.action.common.settings.ProcessorSettings;
import io.metaloom.cortex.action.media.AbstractWorkerTest;
import io.metaloom.cortex.action.media.LoomClientMock;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.cortex.action.thumbnail.ThumbnailAction;
import io.metaloom.loom.cortex.action.thumbnail.ThumbnailActionSettings;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.Testdata;

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
