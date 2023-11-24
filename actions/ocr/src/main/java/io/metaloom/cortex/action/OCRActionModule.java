package io.metaloom.cortex.action;

import dagger.Binds;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;
import dagger.Module;

@Module
public interface OCRActionModule {

	@Binds
	@IntoSet
	abstract CortexAction bindAction(OCRAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(OCROptions.class, "ocr");
	}

}
