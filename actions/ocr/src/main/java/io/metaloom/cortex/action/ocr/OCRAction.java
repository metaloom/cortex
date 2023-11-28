package io.metaloom.cortex.action.ocr;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;

@Singleton
public class OCRAction extends AbstractMediaAction<OCROptions> {

	public static final Logger log = LoggerFactory.getLogger(OCRAction.class);

	@Inject
	public OCRAction(LoomGRPCClient client, CortexOptions options, OCROptions actionOptions) {
		super(client, options, actionOptions);
	}

	@Override
	public String name() {
		return "ocr";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		return ctx.media().isImage();
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		return false;
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws IOException {
		// OCR.ocr(img)
		return ctx.skipped("not implemented").next();
	}

}
