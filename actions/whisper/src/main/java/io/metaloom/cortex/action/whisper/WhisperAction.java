package io.metaloom.cortex.action.whisper;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.rest.model.asset.AssetResponse;

public class WhisperAction extends AbstractMediaAction<WhisperOptions> {

	public static final Logger log = LoggerFactory.getLogger(WhisperAction.class);

	private WhisperMediaProcessor processor;

	public WhisperAction(@Nullable LoomClient client, CortexOptions cortexOptions, WhisperOptions options,  WhisperMediaProcessor processor) {
		super(client, cortexOptions, options);
		this.processor = processor;

	}

	@Override
	public String name() {
		return "whisper";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		if (options().isEnabled()) {
			return ctx.media().isVideo() || ctx.media().isAudio();
		} else {
			return false;
		}
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		// TODO Auto-generated method stub
		return false;
	}
}
