package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;
import static io.metaloom.cortex.api.action.ResultOrigin.REMOTE;
import static io.metaloom.cortex.media.hash.HashMedia.HASH;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.cortex.media.hash.HashMedia;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.rest.model.asset.AssetResponse;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.SHA256;

@Singleton
public class SHA256Action extends AbstractMediaAction<HashActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(SHA256Action.class);

	@Inject
	public SHA256Action(@Nullable LoomClient client, CortexOptions cortexOption, HashActionOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return "sha256";
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
		if (asset != null && asset.getHashes().getSHA256() != null) {
			media.setSHA256(asset.getHashes().getSHA256());
			return ctx.origin(REMOTE).next();
		} else {
			SHA256 hash = HashUtils.computeSHA256(media.file());
			media.setSHA256(hash);
			return ctx.origin(COMPUTED).next();
		}
	}

}
