package io.metaloom.loom.cortex.cli.dagger;

import dagger.Module;
import io.metaloom.cortex.action.OCRActionModule;
import io.metaloom.cortex.action.fp.FingerprintActionModule;
import io.metaloom.cortex.action.hash.HashActionModule;
import io.metaloom.cortex.action.scene.SceneDetectorActionModule;
import io.metaloom.loom.cortex.action.consistency.ConsistencyActionModule;
import io.metaloom.loom.cortex.action.facedetect.FacedetectActionModule;
import io.metaloom.loom.cortex.action.thumbnail.ThumbnailActionModule;
import io.metaloom.loom.cortex.action.tika.TikaActionModule;
import io.metaloom.loom.cortex.dedup.DedupActionModule;

@Module(includes = {
	FingerprintActionModule.class,
	OCRActionModule.class,
	HashActionModule.class,
	FacedetectActionModule.class,
	DedupActionModule.class,
	TikaActionModule.class,
	ThumbnailActionModule.class,
	SceneDetectorActionModule.class,
	ConsistencyActionModule.class })
public interface ActionModule {

}
