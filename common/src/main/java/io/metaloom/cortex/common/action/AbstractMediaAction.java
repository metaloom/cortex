package io.metaloom.cortex.common.action;

import static io.metaloom.cortex.api.action.ResultOrigin.LOCAL;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.api.option.action.CortexActionOptions;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.SHA512;

public abstract class AbstractMediaAction<T extends CortexActionOptions> extends AbstractFilesystemAction<T> {

	public static final Logger log = LoggerFactory.getLogger(AbstractMediaAction.class);

	public AbstractMediaAction(LoomGRPCClient client, CortexOptions cortexOption, T option) {
		super(client, cortexOption, option);
	}

	@Override
	public ActionResult2 process(ActionContext ctx) {
		LoomMedia media = ctx.media();
		Optional<ActionResult2> checkResult = check(ctx);
		if (checkResult != null && checkResult.isPresent()) {
			return checkResult.get();
		}
		if (isProcessed(media)) {
			return ctx.origin(LOCAL).next();
		} else {
			SHA512 sha512 = media.getSHA512();
			AssetResponse asset = client().loadAsset(sha512).sync();
			if (asset == null) {
				return process(ctx, asset);
			} else {
				return update(ctx, asset);
			}
		}
	}

	/**
	 * Run initial preflight checks on the media. The returned result will be passed along. Returning no result will instruct the processor to keep processing
	 * the current media.
	 * 
	 * @param ctx
	 * @return
	 */
	protected Optional<ActionResult2> check(ActionContext ctx) {
		return Optional.empty();
	}

	protected abstract ActionResult2 process(ActionContext ctx, AssetResponse asset);

	/**
	 * Update the media by use of the information which the response provides.
	 * 
	 * @param ctx
	 * @param asset
	 * @return
	 */
	protected abstract ActionResult2 update(ActionContext ctx, AssetResponse asset);

	protected abstract boolean isProcessed(LoomMedia media);

}
