package io.metaloom.cortex.action.captioning;

import java.io.IOException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.rest.model.asset.AssetResponse;

@Singleton
public class CaptioningAction extends AbstractMediaAction<CaptioningActionOptions> {

	private final SmolVLMClient smolvlmClient;

	@Inject
	public CaptioningAction(@Nullable LoomClient client, CortexOptions cortexOption, CaptioningActionOptions option) {
		super(client, cortexOption, option);
		this.smolvlmClient = new SmolVLMClient(option.getSmolVLMHost(), option.getSmolVLMPort());
	}

	@Override
	public String name() {
		return "captioning";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		return ctx.media().isVideo() || ctx.media().isImage();
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		return false;
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws IOException {
		return ctx.skipped("not implemented").next();
	}

}
