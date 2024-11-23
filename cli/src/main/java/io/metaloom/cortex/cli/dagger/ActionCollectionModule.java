package io.metaloom.cortex.cli.dagger;

import dagger.Module;
import io.metaloom.cortex.action.captioning.CaptioningActionModule;
import io.metaloom.cortex.action.consistency.ConsistencyActionModule;
import io.metaloom.cortex.action.dedup.DedupActionModule;
import io.metaloom.cortex.action.facedetect.FacedetectActionModule;
import io.metaloom.cortex.action.fp.FingerprintActionModule;
import io.metaloom.cortex.action.hash.HashActionModule;
import io.metaloom.cortex.action.llm.LLMActionModule;
import io.metaloom.cortex.action.loom.LoomActionModule;
import io.metaloom.cortex.action.ocr.OCRActionModule;
import io.metaloom.cortex.action.scene.SceneDetectionActionModule;
import io.metaloom.cortex.action.thumbnail.ThumbnailActionModule;
import io.metaloom.cortex.action.tika.TikaActionModule;

@Module(includes = {
	HashActionModule.class,
	ThumbnailActionModule.class,
	FingerprintActionModule.class,
	OCRActionModule.class,
	FacedetectActionModule.class,
	DedupActionModule.class,
	TikaActionModule.class,
	LLMActionModule.class,
	SceneDetectionActionModule.class,
	LoomActionModule.class,
	CaptioningActionModule.class,
	ConsistencyActionModule.class })
public interface ActionCollectionModule {

}
