package io.metaloom.cortex.processor.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.processor.MediaProcessor;
import io.metaloom.cortex.scanner.FilesystemProcessor;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.client.grpc.impl.LoomGRPCClientImpl.Builder;

@Singleton
public class DefaultMediaProcessorImpl implements MediaProcessor {

	private final FilesystemProcessor processor;
	private final CortexOptions options;

	public static final Logger log = LoggerFactory.getLogger(DefaultMediaProcessorImpl.class);

	@Inject
	public DefaultMediaProcessorImpl(CortexOptions options, FilesystemProcessor processor) {
		this.options = options;
		this.processor = processor;
	}

	@Override
	public void process(Path folder) throws IOException {
		if (!Files.exists(folder)) {
			throw new FileNotFoundException("Startfolder not found " + folder.toString());
		}

		String hostname = options.getLoom().getHostname();
		int port = options.getLoom().getPort();

		if (hostname == null) {
			log.info("No hostname specified. Processing in offline mode.");
			process(null, folder);
		} else {
			Builder builder = LoomGRPCClient.builder().setHostname(hostname).setPort(port);
			try (LoomGRPCClient client = builder.build()) {
				process(client, folder);
			}
		}
	}

	private void process(LoomGRPCClient client, Path folder) throws IOException {
		processor.analyze(folder);
	}

}
