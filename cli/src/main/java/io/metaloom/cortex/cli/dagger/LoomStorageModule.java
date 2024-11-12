
package io.metaloom.cortex.cli.dagger;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.api.media.type.handler.impl.AvroLoomMetaTypeHandlerImpl;

@Module
public abstract class LoomStorageModule {

	@Binds
	@IntoSet
	abstract LoomMetaTypeHandler bindAvroHandler(AvroLoomMetaTypeHandlerImpl e);
	
}
