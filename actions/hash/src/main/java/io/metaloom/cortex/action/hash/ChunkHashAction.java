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
import io.metaloom.utils.hash.ChunkHash;
import io.metaloom.utils.hash.HashUtils;

@Singleton
public class ChunkHashAction extends AbstractMediaAction<HashOptions> {

	public static final Logger log = LoggerFactory.getLogger(ChunkHashAction.class);

	@Inject
	public ChunkHashAction(LoomGRPCClient client, CortexOptions cortexOption, HashOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return "chunk-hash";
	}

	@Override
	protected boolean isProcessed(LoomMedia media) {
		return media.getChunkHash() != null;
	}

	@Override
	protected ActionResult2 process(ActionContext ctx, AssetResponse asset) {
		LoomMedia media = ctx.media();
		ChunkHash hash = HashUtils.computeChunkHash(media.file());
		media.setChunkHash(hash);
		return ctx.origin(COMPUTED).next();
	}

	@Override
	protected ActionResult2 update(ActionContext ctx, AssetResponse asset) {
		LoomMedia media = ctx.media();
		ChunkHash hash = ChunkHash.fromString(asset.getChunkHash());
		// Check whether the hash was in db
		if (hash != null) {
			media.setChunkHash(hash);
			return ctx.origin(REMOTE).next();
		} else {
			return process(ctx, asset);
		}
	}

}
