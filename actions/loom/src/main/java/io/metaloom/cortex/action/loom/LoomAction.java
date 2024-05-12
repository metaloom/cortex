package io.metaloom.cortex.action.loom;

import static io.metaloom.cortex.media.hash.HashMedia.HASH;

import java.io.IOException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionOrder;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.cortex.media.hash.HashMedia;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetRequest;

@Singleton
public class LoomAction extends AbstractFilesystemAction<LoomActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(LoomAction.class);

	@Inject
	public LoomAction(@Nullable LoomGRPCClient client, CortexOptions cortexOption, LoomActionOptions option) {
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
		if (isOfflineMode()) {
			log.info("Running in offline mode. Skipping action");
			return ctx.next();
		}
		try {
			HashMedia media = ctx.media(HASH);
			AssetRequest.Builder builder = AssetRequest.newBuilder();

			if (media.getSHA512() != null) {
				builder.setSha512Sum(media.getSHA512().toString());
			}
			if (media.getMD5() != null) {
				builder.setMd5Sum(media.getMD5().toString());
			}
			if (media.getSHA256() != null) {
				builder.setSha256Sum(media.getSHA256().toString());
			}

			AssetRequest request = builder.build();
			client().storeAsset(request);
			return ctx.next();
		} catch (Exception e) {
			log.error("Error while storing asset in Loom", e);
			return ctx.failure(e.getMessage()).next();
		}
	}

}
