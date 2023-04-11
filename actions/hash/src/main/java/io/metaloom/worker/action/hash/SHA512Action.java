package io.metaloom.worker.action.hash;

import static io.metaloom.worker.action.api.ActionResult.CONTINUE_NEXT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;
import io.metaloom.worker.action.common.AbstractFilesystemAction;
import io.metaloom.worker.action.common.settings.ProcessorSettings;

public class SHA512Action extends AbstractFilesystemAction<HashActionSettings> {

	public static final Logger log = LoggerFactory.getLogger(SHA512Action.class);

	public SHA512Action(LoomGRPCClient client, ProcessorSettings processorSettings, HashActionSettings settings) {
		super(client, processorSettings, settings);
	}

	private static final String NAME = "sha512-hash";

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(ProcessableMedia media) {
		long start = System.currentTimeMillis();
		media.getHash512();
		print(media, "DONE", "", start);
		return ActionResult.processed(CONTINUE_NEXT, start);
	}

}
