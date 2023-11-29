package io.metaloom.cortex.action.tika;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;

@Singleton
public class TikaAction extends AbstractMediaAction<TikaActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(TikaAction.class);

	public static String DONE_FLAG = "DONE";

	public static String NULL_FLAG = "NULL";

	@Inject
	public TikaAction(LoomGRPCClient client, CortexOptions cortexOption, TikaActionOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return "tika";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		LoomMedia media = ctx.media();
		return media.isImage() || media.isAudio() || media.isVideo() || media.isDocument();
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		LoomMedia media = ctx.media();
		return media.getTikaFlags() != null;
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws Exception {
		LoomMedia media = ctx.media();
		String flags = getFlags(media);
		if (NULL_FLAG.equals(flags)) {
			return ctx.skipped("previously failed").next();
		} else if (flags == null) {
			if (asset == null) {
				return parseMedia(ctx);
				// TODO utilize and store result
			} else {
				// TODO check whether db needs tika update
				return parseMedia(ctx);
				// TODO check response and assert whether processing is needed
				// return done(media, start, "from db");
				// writeFlags(media, DONE_FLAG);
			}
		} else {
			return ctx.info("already processed").next();
		}
	}

	private ActionResult parseMedia(ActionContext ctx) {
		LoomMedia media = ctx.media();
		try {
			String result = MediaTikaParser.parse(media);
			// TODO store result
			media.setTikaFlags(DONE_FLAG);
			return ctx.origin(COMPUTED).next();
		} catch (Exception e) {
			log.error("Error while processing media " + media.path(), e);
			media.setTikaFlags(DONE_FLAG);
			return ctx.failure("failed processing").next();
		}
	}

	private String getFlags(LoomMedia media) {
		String tikaFlags = media.getTikaFlags();
		if (tikaFlags == null) {
			// media.setTikaFlags(tikaFlags);
		}
		return tikaFlags;
	}

}
