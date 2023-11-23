package io.metaloom.cortex.action.scene;

import static io.metaloom.cortex.action.api.ActionResult.CONTINUE_NEXT;
import static io.metaloom.cortex.action.api.ActionResult.SKIP_NEXT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.media.LoomMedia;
import io.metaloom.cortex.action.common.AbstractFilesystemAction;
import io.metaloom.cortex.action.common.settings.ProcessorSettings;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class SceneDetectionAction extends AbstractFilesystemAction<SceneDetectionActionSettings> {

	public static final Logger log = LoggerFactory.getLogger(SceneDetectionAction.class);

	public SceneDetectionAction(LoomGRPCClient client, ProcessorSettings processorSettings, SceneDetectionActionSettings settings) {
		super(client, processorSettings, settings);
	}

	private static final String NAME = "scene-detection";

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(LoomMedia media) {
		long start = System.currentTimeMillis();
		String hash = media.getSHA512();
		if (media.isVideo()) {
			
			print(media, "DONE", "", start);
			return ActionResult.processed(CONTINUE_NEXT, start);
		} else {
			return ActionResult.processed(SKIP_NEXT, start);
		}

	}

}
