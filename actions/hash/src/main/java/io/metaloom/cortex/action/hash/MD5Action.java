package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;
import static io.metaloom.cortex.api.action.ResultOrigin.REMOTE;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.MD5;

@Singleton
public class MD5Action extends AbstractMediaAction<HashOptions> {

	public static final Logger log = LoggerFactory.getLogger(MD5Action.class);

	@Inject
	public MD5Action(LoomGRPCClient client, CortexOptions cortexOption, HashOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return "md5";
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		return ctx.media().getMD5() != null;
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		if (!options().isMD5()) {
			// TODO return or log reason
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) {
		LoomMedia media = ctx.media();
		if (asset != null && asset.getMd5Sum() != null) {
			media.setMD5(MD5.fromString(asset.getMd5Sum()));
			return ctx.origin(REMOTE).next();
		} else {
			MD5 hash = HashUtils.computeMD5(media.file());
			media.setMD5(hash);
			return ctx.origin(COMPUTED).next();
		}
	}

}
