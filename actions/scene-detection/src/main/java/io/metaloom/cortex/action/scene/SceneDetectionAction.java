package io.metaloom.cortex.action.scene;

import static io.metaloom.cortex.api.action.ActionResult.CONTINUE_NEXT;
import static io.metaloom.cortex.api.action.ActionResult.SKIP_NEXT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.utils.hash.SHA512;

public class SceneDetectionAction extends AbstractFilesystemAction<SceneDetectionOptions> {

	public static final Logger log = LoggerFactory.getLogger(SceneDetectionAction.class);

	public SceneDetectionAction(LoomGRPCClient client, CortexOptions cortexOptions, SceneDetectionOptions options) {
		super(client, cortexOptions, options);
	}

	private static final String NAME = "scene-detection";

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(LoomMedia media) {
		long start = System.currentTimeMillis();
		SHA512 hash = media.getSHA512();
		if (media.isVideo()) {
			print(media, "DONE", "", start);
			return ActionResult.processed(CONTINUE_NEXT, start);
		} else {
			return ActionResult.processed(SKIP_NEXT, start);
		}
	}

}
