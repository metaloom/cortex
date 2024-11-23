package io.metaloom.cortex.action.llm;

import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;
import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.media.test.AbstractBasicActionTest;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.test.data.TestMedia;

public class LLMActionTest extends AbstractBasicActionTest<LLMAction> {

	@Test
	public void testProcessing() throws IOException {
		LoomMedia media = mediaVideo1();
		ActionResult result = action().process(media);
		assertThat(result).isSuccess();
		assertThat(media).hasXAttr(1).hasXAttr(SHA_512_KEY, sampleVideoSHA512());

	}

	@Override
	protected void assertProcessed(TestMedia testMedia, LoomMedia media, ActionResult result, LLMAction actionMock) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void disableAction(LLMAction actionMock) {
		LLMActionOptions options = actionMock.options();
		when(options.isEnabled()).thenReturn(false);
	}

	@Override
	public LLMAction mockAction(LoomClient client, CortexOptions cortexOptions) {
		LLMActionOptions options = mock(LLMActionOptions.class);
		when(options.isEnabled()).thenReturn(true);
		when(options.ollamaUrl()).thenReturn(TestEnv.OLLAMA_URL);
		return new LLMAction(null, cortexOptions, options);
	}

}
