package io.metaloom.cortex.action.dedup;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;

@Singleton
public class FingerprintDedupAction extends AbstractMediaAction<DedupActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(FingerprintDedupAction.class);

	@Inject
	public FingerprintDedupAction(LoomGRPCClient client, CortexOptions cortexOptions, DedupActionOptions options, MetaStorage storage) {
		super(client, cortexOptions, options, storage);
	}

	@Override
	public String name() {
		return "fingerprint-dedup";
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		return ctx.media().getFingerprint() != null;
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		return ctx.media().isVideo();
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) {
		return ctx.skipped("not implemented").next();
	}

}
