package io.metaloom.cortex.action.scene;

import static io.metaloom.cortex.api.media.HashMedia.SHA_512_KEY;
import static io.metaloom.cortex.common.test.assertj.CortexAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.SceneDetectionMedia;
import io.metaloom.cortex.api.media.param.SceneDetectionParameter;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractBasicActionTest;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.test.data.TestMedia;
import io.metaloom.video4j.Video4j;

public class SceneDetectionActionTest extends AbstractBasicActionTest<SceneDetectionAction> {

	static {
		Video4j.init();
	}

	@Override
	protected void assertProcessed(TestMedia testMedia, LoomMedia media, ActionResult result, SceneDetectionAction actionMock) {
		assertThat(media).hasXAttr(1).hasXAttr(SHA_512_KEY, testMedia.sha512());
		//assertThat(media).hasXAttr(SceneDetectionMedia.SCENE_DETECTION_FLAG_KEY);

		SceneDetectionParameter detection = media.getSceneDetection();
		System.out.println("Scenes: " + detection.getScenes());
	}

	@Override
	protected void assertProcessedImage(SceneDetectionAction actionMock, LoomMedia media, TestMedia image) throws IOException {
		assertSkipped(actionMock, media);
	}

	@Override
	protected void assertProcessedDoc(SceneDetectionAction actionMock, LoomMedia media, TestMedia docMedia) throws IOException {
		assertSkipped(actionMock, media);
	}

	@Override
	protected void assertProcessedAudio(SceneDetectionAction actionMock, LoomMedia media, TestMedia audio) throws IOException {
		assertSkipped(actionMock, media);
	}

	@Override
	protected void disableAction(SceneDetectionAction actionMock) {
		SceneDetectionOptions options = actionMock.options();
		when(options.isEnabled()).thenReturn(false);
	}

	@Override
	public SceneDetectionAction mockAction(LoomGRPCClient client) {
		SceneDetectionOptions options = mock(SceneDetectionOptions.class);
		when(options.isEnabled()).thenReturn(true);
		return new SceneDetectionAction(client, mock(CortexOptions.class), options);
	}

}
