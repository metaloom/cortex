package io.metaloom.cortex.action.whisper;

import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;
import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;
import static io.metaloom.cortex.media.whisper.WhisperMedia.WHISPER;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.media.test.AbstractBasicActionTest;
import io.metaloom.cortex.media.whisper.WhisperMedia;
import io.metaloom.cortex.media.whisper.WhisperResult;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.test.data.TestMedia;

public class WhisperActionTest extends AbstractBasicActionTest<WhisperAction> {

	@Override
	protected void assertProcessed(TestMedia testMedia, LoomMedia media, ActionResult result, WhisperAction actionMock) {
		WhisperMedia sceneMedia = media.of(WHISPER);
		assertThat(media).hasXAttr(1).hasXAttr(SHA_512_KEY, testMedia.sha512());
		// assertThat(media).hasXAttr(SceneDetectionMedia.SCENE_DETECTION_FLAG_KEY);

		WhisperResult whisperResult = sceneMedia.getWhisperResult();
		System.out.println("Segments: " + whisperResult.segments());
	}

	@Override
	protected void assertProcessedImage(WhisperAction actionMock, LoomMedia media, TestMedia image) throws IOException {
		assertSkipped(actionMock, media);
	}

	@Override
	protected void assertProcessedDoc(WhisperAction actionMock, LoomMedia media, TestMedia docMedia) throws IOException {
		assertSkipped(actionMock, media);
	}

	@Override
	protected void assertProcessedAudio(WhisperAction actionMock, LoomMedia media, TestMedia audio) throws IOException {
		assertSkipped(actionMock, media);
	}

	@Override
	protected void disableAction(WhisperAction actionMock) {
		WhisperOptions options = actionMock.options();
		when(options.isEnabled()).thenReturn(false);
	}

	@Override
	public WhisperAction mockAction(LoomClient client, CortexOptions cortexOptions) {
		WhisperOptions options = mock(WhisperOptions.class);
		when(options.isEnabled()).thenReturn(true);
		WhisperMediaProcessor processor = new WhisperMediaProcessor(options);
		return new WhisperAction(client, cortexOptions, options, processor);
	}

}
