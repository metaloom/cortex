package io.metaloom.cortex.action.scene;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class SceneDetectionActionModule extends AbstractActionModule {

	@Binds
	@IntoSet
	abstract FilesystemAction bindAction(SceneDetectionAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(SceneDetectionOptions.class, "scene-detector");
	}

	@Provides
	public static SceneDetectionOptions options(CortexOptions options) {
		return actionOptions(options, "scene-detector", new SceneDetectionOptions());
	}
}
