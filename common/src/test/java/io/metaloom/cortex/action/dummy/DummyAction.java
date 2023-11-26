package io.metaloom.cortex.action.dummy;

import java.io.IOException;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class DummyAction extends AbstractFilesystemAction<DummyOptions> {

	boolean invoked = false;

	public DummyAction(LoomGRPCClient client, CortexOptions cortexOption, DummyOptions option) {
		super(client, cortexOption, option);
	}

	@Override
	public ActionResult2 process(ActionContext ctx) throws IOException {
		invoked = true;
		return ctx.skipped().next();
	}

	@Override
	public String name() {
		return "dummy";
	}

	public boolean wasInvoked() {
		return invoked;
	}
}
