package io.metaloom.cortex.action.scene;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;
import static io.metaloom.cortex.media.scene.SceneDetectionMedia.SCENE_DETECTION;

import java.io.IOException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.scene.impl.OpticalFlowSceneDetector;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.cortex.media.scene.SceneDetectionMedia;
import io.metaloom.cortex.media.scene.SceneDetectionResult;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.video4j.VideoFile;

@Singleton
public class SceneDetectionAction extends AbstractMediaAction<SceneDetectionOptions> {

	public static final Logger log = LoggerFactory.getLogger(SceneDetectionAction.class);

	private OpticalFlowSceneDetector detector = new OpticalFlowSceneDetector();

	@Inject
	public SceneDetectionAction(@Nullable LoomGRPCClient client, CortexOptions cortexOptions, SceneDetectionOptions options) {
		super(client, cortexOptions, options);
	}

	@Override
	public String name() {
		return "scene-detection";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		if (options().isEnabled()) {
			return ctx.media().isVideo();
		} else {
			return false;
		}
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		return ctx.media(SCENE_DETECTION).hasSceneDetection();
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws IOException {
		SceneDetectionMedia media = ctx.media(SCENE_DETECTION);
		if (media.isVideo()) {
			VideoFile video = VideoFile.open(media.path());
			SceneDetectionResult result = detector.detect(video);
			media.setSceneDetection(result);
			return ctx.origin(COMPUTED).next();
		} else {
			return ctx.skipped("no video media").next();
		}
	}

}
