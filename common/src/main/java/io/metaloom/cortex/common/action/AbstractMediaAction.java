package io.metaloom.cortex.common.action;

import static io.metaloom.cortex.api.action.ResultOrigin.LOCAL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
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
	public ActionResult process(ActionContext ctx) {
		LoomMedia media = ctx.media();
		if (!media.exists()) {
			return ctx.failure("File " + media.path() + " not found").abort();
		}
		if (isProcessable(ctx)) {
			if (isProcessed(ctx)) {
				return ctx.skipped("Already computed").origin(LOCAL).next();
			} else {
				try {
					if (isOfflineMode()) {
						// Invoke the actual computation of the needed data by the action implementation
						return compute(ctx, null);
					} else {
						SHA512 sha512 = media.getSHA512();
						AssetResponse asset = client().loadAsset(sha512).sync();
						return compute(ctx, asset);
					}
				} catch (Exception e) {
					// TODO encode msg
					return ctx.failure(e.getMessage()).next();
				}
			}
		} else {
			return ctx.skipped("unprocessable").next();
		}

	}

	/**
	 * Check whether the media in the context is processable by the action implementation.
	 * 
	 * @param ctx
	 * @return
	 */
	protected abstract boolean isProcessable(ActionContext ctx);

	/**
	 * Check whether the media in the context has already been processed and the needed value is present.
	 * 
	 * @param ctx
	 * @return
	 */
	protected abstract boolean isProcessed(ActionContext ctx);

	/**
	 * Update the media by use of the information which the response provides.
	 * 
	 * @param ctx
	 * @param asset
	 *            The provided response may be utilized to skip the actual computation and just use the loaded data instead
	 * @return
	 */
	protected abstract ActionResult compute(ActionContext ctx, AssetResponse asset) throws Exception;

}
