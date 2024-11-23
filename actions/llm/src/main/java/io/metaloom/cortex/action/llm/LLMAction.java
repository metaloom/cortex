package io.metaloom.cortex.action.llm;

import javax.annotation.Nullable;
import javax.inject.Inject;

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
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.rest.model.asset.AssetResponse;

public class LLMAction extends AbstractMediaAction<LLMActionOptions> {

	@Inject
	public LLMAction(@Nullable LoomClient client, CortexOptions cortexOption, LLMActionOptions options) {
		super(client, cortexOption, options);
		options.setOllamaUrl("http://127.0.0.1:11434");
	}

	@Override
	public String name() {
		return "llm";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		return true;
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		return ctx.media().hasSHA512();
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws Exception {
		// TODO make model configurable
		LargeLanguageModel model = new LargeLanguageModelImpl("gemma2:27b", options().ollamaUrl(), 2048, LLMProviderType.OLLAMA);
		Prompt prompt = new PromptImpl("Write hello world");
		LLMContext llmCtx = LLMContext.ctx(prompt, model);

		LLMProvider provider = new OllamaLLMProvider();
		String text = provider.generate(llmCtx);
		System.out.println(text);
		return null;
	}

}
