package io.metaloom.cortex.action.llm;

import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.media.test.AbstractBasicActionTest;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.test.data.TestMedia;
import io.vertx.core.json.JsonObject;

public class LLMActionTest extends AbstractBasicActionTest<LLMAction> {

	@Test
	public void testProcessing() throws IOException {
		LoomMedia media = mediaVideo1();
		ActionResult result = action().process(media);
		assertThat(result).isSuccess();
		String jsonStr = """
			{"format":"mp4","genre":null,"year":null,"title":"pexels-jack-sparrow-5977265"}
			""";

		JsonObject json = new JsonObject(jsonStr);
		assertThat(media).hasXAttr(1).hasXAttr(LLMAction.resultKey("default"), json);
	}

	@Override
	protected void assertProcessed(TestMedia testMedia, LoomMedia media, ActionResult result, LLMAction actionMock) {
		assertTrue(media.has(LLMAction.resultKey("default")));
	}

	@Override
	protected void disableAction(LLMAction actionMock) {
		LLMActionOptions options = actionMock.options();
		options.setEnabled(false);
	}

	@Override
	public LLMAction mockAction(LoomClient client, CortexOptions cortexOptions) {
		LLMActionOptions options = new LLMActionOptions();
		options.setOllamaUrl(TestEnv.OLLAMA_URL);
		options.setEnabled(true);
		return new LLMAction(null, cortexOptions, options);
	}

}
