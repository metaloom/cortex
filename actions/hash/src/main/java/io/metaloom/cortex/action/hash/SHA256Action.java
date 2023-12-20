package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;
import static io.metaloom.cortex.api.action.ResultOrigin.REMOTE;
import static io.metaloom.cortex.media.hash.HashMedia.HASH;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.cortex.media.hash.HashMedia;
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
	protected boolean isProcessed(ActionContext ctx) {
		return ctx.media(HASH).hasSHA256();
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		if (options().isSHA256()) {
			return true;
		} else {
			// TODO log or return reason
			return false;
		}
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) {
		HashMedia media = ctx.media().of(HASH);
		if (asset != null && asset.getSha256Sum() != null) {
			media.setSHA256( SHA256.fromString(asset.getSha256Sum()));
			return ctx.origin(REMOTE).next();
		} else {
			SHA256 hash = HashUtils.computeSHA256(media.file());
			media.setSHA256(hash);
			return ctx.origin(COMPUTED).next();
		}
	}

}
