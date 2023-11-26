package io.metaloom.cortex.action.ocr;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

@Singleton
public class OCRAction extends AbstractFilesystemAction<OCROptions> {

	public static final Logger log = LoggerFactory.getLogger(OCRAction.class);

	private static final String NAME = "ocr";

	@Inject
	public OCRAction(LoomGRPCClient client, CortexOptions options, OCROptions actionOptions) {
		super(client, options, actionOptions);
	}

	@Override
	public ActionResult process(LoomMedia media) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String name() {
		return NAME;
	}

}
