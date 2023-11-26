package io.metaloom.cortex.action.dedup;

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
public class FingerprintDedupAction extends AbstractFilesystemAction<DedupActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(FingerprintDedupAction.class);

	@Inject
	public FingerprintDedupAction(LoomGRPCClient client, CortexOptions cortexOptions, DedupActionOptions options) {
		super(client, cortexOptions, options);
	}
	
	@Override
	public String name() {
		return "fingerprint-dedup";
	}

	@Override
	public ActionResult process(LoomMedia media) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}