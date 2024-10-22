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
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.rest.model.asset.AssetUpdateRequest;
import io.metaloom.utils.hash.SHA512;

@Singleton
public class LoomAction extends AbstractFilesystemAction<LoomActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(LoomAction.class);

	@Inject
	public LoomAction(@Nullable LoomClient client, CortexOptions cortexOption, LoomActionOptions option) {
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
			AssetUpdateRequest request = new AssetUpdateRequest();

			if (media.getSHA512() != null) {
				request.getHashes().setSHA512(media.getSHA512());
			}
			if (media.getMD5() != null) {
				request.getHashes().setMD5(media.getMD5());
			}
			if (media.getSHA256() != null) {
				request.getHashes().setSHA256(media.getSHA256());
			}

			SHA512 hash = media.getSHA512();
			client().updateAsset(hash, request);
			return ctx.next();
		} catch (Exception e) {
			log.error("Error while storing asset in Loom", e);
			return ctx.failure(e.getMessage()).next();
		}
	}

}
