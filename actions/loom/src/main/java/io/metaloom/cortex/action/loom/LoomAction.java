package io.metaloom.cortex.action.loom;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionOrder;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetRequest;

@Singleton
public class LoomAction extends AbstractFilesystemAction<LoomActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(LoomAction.class);

	@Inject
	public LoomAction(LoomGRPCClient client, CortexOptions cortexOption, LoomActionOptions option) {
		super(client, cortexOption, option);
	}

	@Override
	public String name() {
		return "loom";
	}

	@Override
	public ActionOrder order() {
		return ActionOrder.LAST;
	}

	@Override
	public ActionResult process(ActionContext ctx) throws IOException {
		LoomMedia media = ctx.media();

		AssetRequest.Builder request = AssetRequest.newBuilder()
			.setSha512Sum(media.getSHA512().toString())
			.setMd5Sum(media.getMD5().toString())
			.setSha256Sum(media.getSHA256().toString());

		client().storeAsset(request.build());
		return ctx.next();
	}

}
