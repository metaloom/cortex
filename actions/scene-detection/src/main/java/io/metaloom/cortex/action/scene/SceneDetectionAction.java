package io.metaloom.cortex.action.scene;

import static io.metaloom.cortex.api.action.ActionResult2.CONTINUE_NEXT;
import static io.metaloom.cortex.api.action.ActionResult2.SKIP_NEXT;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.utils.hash.SHA512;

@Singleton
public class SceneDetectionAction extends AbstractFilesystemAction<SceneDetectionOptions> {

	public static final Logger log = LoggerFactory.getLogger(SceneDetectionAction.class);

	@Inject
	public SceneDetectionAction(LoomGRPCClient client, CortexOptions cortexOptions, SceneDetectionOptions options) {
		super(client, cortexOptions, options);
	}

	@Override
	public String name() {
		return "scene-detection";
	}

	@Override
	public ActionResult2 process(ActionContext ctx) {
		LoomMedia media = ctx.media();
		SHA512 hash = media.getSHA512();
		if (media.isVideo()) {
			return ctx.next();
		} else {
			return ctx.skipped().next();
		}
	}

}
