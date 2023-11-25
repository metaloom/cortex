package io.metaloom.cortex.action.dummy;

import java.io.IOException;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class DummyAction extends AbstractFilesystemAction<DummyOptions> {

	boolean invoked = false;

	public DummyAction(LoomGRPCClient client, CortexOptions cortexOption, DummyOptions option) {
		super(client, cortexOption, option);
	}

	@Override
	public ActionResult process(LoomMedia media) throws IOException {
		invoked = true;
		return ActionResult.skipped(true, 0L);
	}

	@Override
	public String name() {
		return "dummy";
	}

	public boolean wasInvoked() {
		return invoked;
	}
}
