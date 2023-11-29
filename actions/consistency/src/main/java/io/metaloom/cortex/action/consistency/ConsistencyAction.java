package io.metaloom.cortex.action.consistency;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;
import static io.metaloom.cortex.api.action.ResultOrigin.REMOTE;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.partial.PartialFile;

@Singleton
public class ConsistencyAction extends AbstractMediaAction<ConsistencyActionOptions> {

	@Inject
	public ConsistencyAction(LoomGRPCClient client, CortexOptions cortexOption, ConsistencyActionOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return "consistency";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		LoomMedia media = ctx.media();
		// ctx.skipped("no video or audio media").next();
		return media.isVideo() && media.isAudio();
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		LoomMedia media = ctx.media();
		Long count = media.getZeroChunkCount();
		return count != null;
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws Exception {
		LoomMedia media = ctx.media();

		if (asset == null) {
			// if (!isOfflineMode()) {
			computeSum(media);
			return ctx.origin(COMPUTED).next();

		} else {
			Long dbCount = asset.getZeroChunkCount();
			if (dbCount != null) {
				media.setZeroChunkCount(dbCount);
				return ctx.origin(REMOTE).next();
			} else {
				computeSum(media);
				return ctx.origin(COMPUTED).next();
			}
		}

	}

	private void computeSum(LoomMedia media) throws NoSuchAlgorithmException, IOException {
		PartialFile pf = new PartialFile(media.path());
		long count = pf.computeZeroChunkCount();
		media.setZeroChunkCount(count);
	}

}
