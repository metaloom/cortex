package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;
import static io.metaloom.cortex.api.action.ResultOrigin.REMOTE;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.ChunkHash;
import io.metaloom.utils.hash.HashUtils;

@Singleton
public class ChunkHashAction extends AbstractMediaAction<HashOptions> {

	public static final Logger log = LoggerFactory.getLogger(ChunkHashAction.class);

	@Inject
	public ChunkHashAction(LoomGRPCClient client, CortexOptions cortexOption, HashOptions options, MetaStorage storage) {
		super(client, cortexOption, options, storage);
	}

	@Override
	public String name() {
		return "chunk-hash";
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		return ctx.media().getChunkHash() != null;
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		if (!options().isChunkHash()) {
			// TODO return or log reason
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) {
		LoomMedia media = ctx.media();
		if (asset != null && asset.getChunkHash() != null) {
			media.setChunkHash(ChunkHash.fromString(asset.getChunkHash()));
			return ctx.origin(REMOTE).next();
		} else {
			ChunkHash hash = HashUtils.computeChunkHash(media.file());
			media.setChunkHash(hash);
			return ctx.origin(COMPUTED).next();
		}
	}

}
