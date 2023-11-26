package io.metaloom.cortex.action.ocr;

import static io.metaloom.cortex.api.action.ActionResult2.CONTINUE_NEXT;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

@Singleton
public class OCRAction extends AbstractFilesystemAction<OCROptions> {

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
	public ActionResult2 process(ActionContext ctx) throws IOException {
		return ctx.skipped().next();
	}


}
