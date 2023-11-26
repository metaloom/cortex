package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;
import static io.metaloom.cortex.api.action.ResultOrigin.REMOTE;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.SHA256;

@Singleton
public class SHA256Action extends AbstractMediaAction<HashOptions> {

	public static final Logger log = LoggerFactory.getLogger(SHA256Action.class);

	@Inject
	public SHA256Action(LoomGRPCClient client, CortexOptions cortexOption, HashOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return "SHA256";
	}

	@Override
	protected boolean isProcessed(LoomMedia media) {
		return media.getSHA256() != null;
	}

	@Override
	protected ActionResult2 process(ActionContext ctx, AssetResponse asset) {
		LoomMedia media = ctx.media();
		SHA256 hash = HashUtils.computeSHA256(media.file());
		media.setSHA256(hash);
		return ctx.origin(COMPUTED).next();
	}

	@Override
	protected ActionResult2 update(ActionContext ctx, AssetResponse asset) {
		LoomMedia media = ctx.media();
		SHA256 hash = SHA256.fromString(asset.getSha256Sum());
		// Check whether the hash was in db
		if (hash != null) {
			media.setSHA256(hash);
			return ctx.origin(REMOTE).next();
		} else {
			return process(ctx, asset);
		}
	}

}
