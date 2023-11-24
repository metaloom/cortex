package io.metaloom.loom.cortex.action.facedetect;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public interface FacedetectActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(FacedetectAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(FacedetectOptions.class, "facedetection");
	}

}
