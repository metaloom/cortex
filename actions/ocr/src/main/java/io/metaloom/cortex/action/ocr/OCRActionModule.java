package io.metaloom.cortex.action.ocr;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;

@Module
public abstract class OCRActionModule extends AbstractActionModule {

	@Binds
	@IntoSet
	abstract FilesystemAction bindAction(OCRAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(OCRActionOptions.class, OCRActionOptions.KEY);
	}

	@Provides
	public static OCRActionOptions options(CortexOptions options) {
		return actionOptions(options, OCRActionOptions.KEY, new OCRActionOptions());
	}

}
