package io.metaloom.cortex.processor.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.processor.MediaProcessor;
import io.metaloom.cortex.scanner.FilesystemProcessor;
import io.metaloom.loom.client.common.LoomClient;

@Singleton
public class DefaultMediaProcessorImpl implements MediaProcessor {

	private final CortexOptions options;
	private final LoomClient client;
	private final FilesystemProcessor processor;

	public static final Logger log = LoggerFactory.getLogger(DefaultMediaProcessorImpl.class);

	@Inject
	public DefaultMediaProcessorImpl(CortexOptions options, @Nullable LoomClient client, FilesystemProcessor processor) {
		this.options = options;
		this.client = client;
		this.processor = processor;
	}

	@Override
	public void process(List<String> enabledActions, Path folder) throws IOException {
		if (!Files.exists(folder)) {
			throw new FileNotFoundException("Startfolder not found " + folder.toString());
		}
		process(enabledActions, client, folder);
	}

	private void process(List<String> enabledActions, LoomClient client, Path folder) throws IOException {
		processor.analyze(enabledActions, folder);
	}

}
