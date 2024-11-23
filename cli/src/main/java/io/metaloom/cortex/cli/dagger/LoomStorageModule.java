
package io.metaloom.cortex.cli.dagger;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.api.media.type.handler.impl.AvroLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.media.type.handler.impl.FSLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.media.type.handler.impl.HeapLoomMetaTypeHandlerImpl;
import io.metaloom.cortex.api.media.type.handler.impl.XAttrLoomMetaTypeHandlerImpl;

@Module
public abstract class LoomStorageModule {

	@Binds
	@IntoSet
	abstract LoomMetaTypeHandler bindAvroHandler(AvroLoomMetaTypeHandlerImpl e);

	@Binds
	@IntoSet
	abstract LoomMetaTypeHandler bindXattrHandler(XAttrLoomMetaTypeHandlerImpl e);

	@Binds
	@IntoSet
	abstract LoomMetaTypeHandler bindFSHandler(FSLoomMetaTypeHandlerImpl e);

	@Binds
	@IntoSet
	abstract LoomMetaTypeHandler bindHeapHandler(HeapLoomMetaTypeHandlerImpl e);

}
