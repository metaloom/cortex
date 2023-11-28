package io.metaloom.cortex.action.loom;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

@Singleton
public class LoomAction extends AbstractFilesystemAction<LoomActionOptions> {

	@Inject
	public LoomAction(LoomGRPCClient client, CortexOptions cortexOption, LoomActionOptions option) {
		super(client, cortexOption, option);
	}

	public static final Logger log = LoggerFactory.getLogger(LoomAction.class);

	@Override
	public String name() {
		return "loom";
	}

	@Override
	public ActionResult process(ActionContext ctx) throws IOException {
		return ctx.next();
	}

}
