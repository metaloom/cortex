package io.metaloom.cortex.action.llm;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.type.LoomMetaCoreType.XATTR;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.ai.genai.llm.LLMContext;
import io.metaloom.ai.genai.llm.LLMProvider;
import io.metaloom.ai.genai.llm.LLMProviderType;
import io.metaloom.ai.genai.llm.LargeLanguageModel;
import io.metaloom.ai.genai.llm.impl.LargeLanguageModelImpl;
import io.metaloom.ai.genai.llm.ollama.OllamaLLMProvider;
import io.metaloom.ai.genai.llm.prompt.Prompt;
import io.metaloom.ai.genai.llm.prompt.impl.PromptImpl;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.rest.model.asset.AssetResponse;
import io.vertx.core.json.JsonObject;

@Singleton
public class LLMAction extends AbstractMediaAction<LLMActionOptions> {

	@Inject
	public LLMAction(@Nullable LoomClient client, CortexOptions cortexOption, LLMActionOptions options) {
		super(client, cortexOption, options);
		if (options.getPrompts().isEmpty()) {
			options.setPrompts(defaultPrompts());
		}
	}

	private Map<String, LLMActionPrompt> defaultPrompts() {
		LLMActionPrompt prompt = new LLMActionPrompt();
		prompt.setModel("gemma2:27b");
		prompt.setPrompt("""
			Extract metadata from the given filename and output JSON.

			Example JSON Format:
			{
				"format": "1080p",
				"genre": "action",
				"year": "2024",
				"title": "The human readable title"
			}
			Filename:
			${name}
			""");
		return Map.of("default", prompt);
	}

	@Override
	public String name() {
		return "llm";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		// Every media has a filename thus can be processed.
		return true;
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		boolean hasAllPrompts = false;
		for (String id : options().getPrompts().keySet()) {
			hasAllPrompts |= ctx.media().has(resultKey(id));
		}
		return hasAllPrompts;
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws Exception {

		for (Entry<String, LLMActionPrompt> entry : options().getPrompts().entrySet()) {
			String promptId = entry.getKey();
			String modelName = entry.getValue().getModel();
			String promptStr = entry.getValue().getPrompt();

			LoomMetaKey<JsonObject> metaKey = resultKey(promptId); 
			boolean hasMetaKey = ctx.media().has(metaKey);
			if (hasMetaKey) {
				continue;
			}

			LargeLanguageModel model = new LargeLanguageModelImpl(modelName, options().ollamaUrl(), 2048, LLMProviderType.OLLAMA);
			Prompt prompt = new PromptImpl(promptStr);
			prompt.set("name", ctx.media().file().getName());
			LLMContext llmCtx = LLMContext.ctx(prompt, model);

			LLMProvider provider = new OllamaLLMProvider();
			JsonObject json = provider.generateJson(llmCtx);

			ctx.media().put(metaKey, json);
		}

		return ActionResult.processed(true);
	}

	public static LoomMetaKey<JsonObject> resultKey(String promptId) {
		return metaKey("llm_result_" + promptId, 1, XATTR, JsonObject.class);
	}

}
