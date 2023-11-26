package io.metaloom.cortex.action.captioning;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

@Singleton
public class CaptioningAction extends AbstractFilesystemAction<CaptioningActionOptions> {

	@Inject
	public CaptioningAction(LoomGRPCClient client, CortexOptions cortexOption, CaptioningActionOptions option) {
		super(client, cortexOption, option);
	}

	@Override
	public String name() {
		return "captioning";
	}

	@Override
	public ActionResult2 process(ActionContext ctx) throws IOException {
		return ctx.skipped().next();
	}

}
