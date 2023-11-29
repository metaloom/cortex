package io.metaloom.cortex.action.captioning;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;

@Singleton
public class CaptioningAction extends AbstractMediaAction<CaptioningActionOptions> {

	@Inject
	public CaptioningAction(LoomGRPCClient client, CortexOptions cortexOption, CaptioningActionOptions option) {
		super(client, cortexOption, option);
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
