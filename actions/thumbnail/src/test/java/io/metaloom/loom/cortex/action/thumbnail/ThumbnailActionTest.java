package io.metaloom.loom.cortex.action.thumbnail;

import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;
import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;
import static io.metaloom.cortex.media.thumbnail.ThumbnailMedia.THUMBNAIL_FLAG_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.thumbnail.ThumbnailAction;
import io.metaloom.cortex.action.thumbnail.ThumbnailActionOptions;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.cortex.common.action.media.LoomClientMock;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.client.common.LoomClientException;

public class ThumbnailActionTest extends AbstractMediaTest {

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
	public void testAction() throws IOException, LoomClientException {
		ThumbnailAction action = mockAction();
		LoomMedia media = mediaVideo3();
		ActionResult result = action.process(ctx(media));
		assertThat(result).isSuccess();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512_KEY).hasXAttr(THUMBNAIL_FLAG_KEY);
		assertThat(new File(thumbnailDir, media.getSHA512() + ".jpg")).exists();
	}

	public ThumbnailAction mockAction() throws LoomClientException {
		LoomClient client = LoomClientMock.mockClient();

		ThumbnailActionOptions options = new ThumbnailActionOptions();
		return new ThumbnailAction(client, new CortexOptions(), options);
	}
}
