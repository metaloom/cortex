package io.metaloom.cortex.action.facedetect;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class FacedetectActionModule extends AbstractActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(FacedetectAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(FacedetectActionOptions.class, FacedetectActionOptions.KEY);
	}

	@Provides
	public static FacedetectActionOptions options(CortexOptions options) {
		return actionOptions(options,  FacedetectActionOptions.KEY, new FacedetectActionOptions());
	}
}
