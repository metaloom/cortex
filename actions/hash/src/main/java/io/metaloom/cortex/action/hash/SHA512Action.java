package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.action.ActionResult.CONTINUE_NEXT;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.ProcessorSettings;
import io.metaloom.cortex.api.option.action.ActionOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.SHA512;

@Singleton
public class SHA512Action extends AbstractFilesystemAction {

	public static final Logger log = LoggerFactory.getLogger(SHA512Action.class);

	@Inject
	public SHA512Action(LoomGRPCClient client, ProcessorSettings processorSettings, ActionOptions options) {
		super(client, processorSettings, options);
	}

	private static final String NAME = "sha512-hash";

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(LoomMedia media) {
		long start = System.currentTimeMillis();
		SHA512 hash = media.getSHA512();
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
