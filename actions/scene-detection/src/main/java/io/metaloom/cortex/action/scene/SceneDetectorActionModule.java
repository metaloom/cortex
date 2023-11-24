package io.metaloom.cortex.action.scene;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public interface SceneDetectorActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(SceneDetectionAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(SceneDetectionOptions.class, "scene-detector");
	}
}
