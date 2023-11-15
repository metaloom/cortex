package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.action.api.ActionResult.CONTINUE_NEXT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.ProcessableMedia;
import io.metaloom.cortex.action.common.AbstractFilesystemAction;
import io.metaloom.cortex.action.common.settings.ProcessorSettings;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;

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
		String hash = media.getHash512();
		AssetResponse asset = client().loadAsset(hash).sync();
		String info = "";
		if (asset == null) {
			info = "new";
			asset = client().storeAsset(hash).sync();
		} else {
			info = "existing";
			// TODO update file info (path...) etc
			// asset.
		}

		print(media, "DONE", "", start);
		return ActionResult.processed(CONTINUE_NEXT, start);
	}

}
