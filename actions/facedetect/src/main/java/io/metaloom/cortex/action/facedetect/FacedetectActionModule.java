package io.metaloom.cortex.action.facedetect;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public interface FacedetectActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(FacedetectAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(FacedetectActionOptions.class, "facedetection");
	}

	@Provides
	public static FacedetectActionOptions options(CortexOptions options) {
		return (FacedetectActionOptions) options.getActions().get("facedetection");
	}
}
