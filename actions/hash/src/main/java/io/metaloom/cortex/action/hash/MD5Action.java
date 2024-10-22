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
import io.metaloom.utils.hash.MD5;

@Singleton
public class MD5Action extends AbstractMediaAction<HashOptions> {

	public static final Logger log = LoggerFactory.getLogger(MD5Action.class);

	@Inject
	public MD5Action(@Nullable LoomClient client, CortexOptions cortexOption, HashOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return "md5";
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		return ctx.media(HASH).hasMD5();
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		if (options().isMD5()) {
			// TODO return or log reason
			return true;
		} else {
			log.debug("[{}] MD5 not enabled in hash options", this);
			return false;
		}
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) {
		HashMedia media = ctx.media(HASH);
		if (asset != null && asset.getHashes().getMD5() != null) {
			media.setMD5(asset.getHashes().getMD5());
			return ctx.origin(REMOTE).next();
		} else {
			MD5 hash = HashUtils.computeMD5(media.file());
			media.setMD5(hash);
			return ctx.origin(COMPUTED).next();
		}
	}

}
