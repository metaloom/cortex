package io.metaloom.cortex.action.scene;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.scene.detector.SceneDetectionResult;
import io.metaloom.cortex.action.scene.impl.OpticalFlowSceneDetector;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.SHA512;
import io.metaloom.video4j.VideoFile;

@Singleton
public class SceneDetectionAction extends AbstractMediaAction<SceneDetectionOptions> {

	public static final Logger log = LoggerFactory.getLogger(SceneDetectionAction.class);

	private OpticalFlowSceneDetector detector = new OpticalFlowSceneDetector();
	
	@Inject
	public SceneDetectionAction(LoomGRPCClient client, CortexOptions cortexOptions, SceneDetectionOptions options, MetaStorage storage) {
		super(client, cortexOptions, options, storage);
	}

	@Override
	public String name() {
		return "scene-detection";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		return ctx.media().isVideo();
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		// TODO check flags
		return false;
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws IOException {
		LoomMedia media = ctx.media();
		SHA512 hash = media.getSHA512();
		if (media.isVideo()) {
			VideoFile video = VideoFile.open(media.path());
			SceneDetectionResult result = detector.detect(video);
			return ctx.origin(COMPUTED).next();
		} else {
			return ctx.skipped("no video media").next();
		}
	}

}
