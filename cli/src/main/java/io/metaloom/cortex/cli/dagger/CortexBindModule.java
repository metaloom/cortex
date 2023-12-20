package io.metaloom.cortex.cli.dagger;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.common.meta.MetaStorageImpl;
import io.metaloom.cortex.impl.CortexImpl;
import io.metaloom.cortex.processor.MediaProcessor;
import io.metaloom.cortex.processor.impl.DefaultMediaProcessorImpl;
import io.metaloom.cortex.scanner.FilesystemProcessor;
import io.metaloom.cortex.scanner.impl.FilesystemProcessorImpl;
import io.metaloom.fs.linux.LinuxFilesystemScanner;
import io.metaloom.fs.linux.impl.LinuxFilesystemScannerImpl;

@Module
public abstract class CortexBindModule {

	@Binds
	@Singleton
	abstract Cortex bindCortex(CortexImpl e);

	@Binds
	@Singleton
	abstract MediaProcessor bindMediaProcessor(DefaultMediaProcessorImpl e);

	@Binds
	@Singleton
	abstract FilesystemProcessor bindFilesystemProcessor(FilesystemProcessorImpl e);

	@Binds
	@Singleton
	abstract MetaStorage bindMetaStorage(MetaStorageImpl e);

	@Provides
	public static LinuxFilesystemScanner bindFilesystemScanner() {
		return new LinuxFilesystemScannerImpl();
	}

}
