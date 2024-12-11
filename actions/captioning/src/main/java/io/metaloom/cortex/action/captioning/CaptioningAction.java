package io.metaloom.cortex.action.captioning;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.type.LoomMetaCoreType.XATTR;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.rest.model.asset.AssetResponse;
import io.metaloom.video4j.utils.ImageUtils;

@Singleton
public class CaptioningAction extends AbstractMediaAction<CaptioningActionOptions> {

	private final SmolVLMClient smolvlmClient;

	public static final LoomMetaKey<String> CAPTION_META_KEY = metaKey("caption_result", 1, XATTR, String.class);

	@Inject
	public CaptioningAction(@Nullable LoomClient client, CortexOptions cortexOption, CaptioningActionOptions option) {
		super(client, cortexOption, option);
		this.smolvlmClient = new SmolVLMClient(option.getSmolVLMHost(), option.getSmolVLMPort());
	}

	@Override
	public String name() {
		return "captioning";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		return ctx.media().isVideo() || ctx.media().isImage();
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		return ctx.media().has(CAPTION_META_KEY);
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws IOException {
		LoomMedia media = ctx.media();
		try {
			if (ctx.media().isImage()) {
				BufferedImage image = ImageUtils.load(media.file());
				String result = smolvlmClient.captionByImage(image, 512);
				media.put(CAPTION_META_KEY, result);
				return ctx.next();
			} else if (ctx.media().isVideo()) {
				return ctx.skipped("not implemented").next();
			} else if (ctx.media().isAudio()) {
				return ctx.skipped("not implemented").next();
			} else {
				return ActionResult.failed(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ActionResult.failed(true);
		}
	}

}
