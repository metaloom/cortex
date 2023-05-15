package io.metaloom.cortex.processor.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.api.FilesystemAction;
import io.metaloom.cortex.action.fp.FingerprintAction;
import io.metaloom.cortex.action.hash.ChunkHashAction;
import io.metaloom.cortex.action.hash.MD5Action;
import io.metaloom.cortex.action.hash.SHA256Action;
import io.metaloom.cortex.action.hash.SHA512Action;
import io.metaloom.cortex.processor.MediaProcessor;
import io.metaloom.cortex.scanner.FilesystemProcessor;
import io.metaloom.cortex.scanner.FilesystemProcessorSetting;
import io.metaloom.cortex.scanner.impl.FilesystemProcessorImpl;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.client.grpc.impl.LoomGRPCClientImpl.Builder;
import io.metaloom.loom.cortex.action.consistency.ConsistencyAction;

public class DefaultMediaProcessorImpl implements MediaProcessor {

	private final FilesystemProcessor processor;
	private final FilesystemProcessorSetting settings;

	public static final Logger log = LoggerFactory.getLogger(DefaultMediaProcessorImpl.class);

	public DefaultMediaProcessorImpl(FilesystemProcessorSetting settings) {
		this.settings = settings;
		this.processor = new FilesystemProcessorImpl();
	}

	@Override
	public void process(Path folder) throws IOException {
		if (!Files.exists(folder)) {
			throw new FileNotFoundException("Startfolder not found " + folder.toString());
		}

		String hostname = settings.getProcessorSettings().getHostname();
		int port = settings.getProcessorSettings().getPort();
	
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
		registerAction(new SHA512Action(client, settings.getProcessorSettings(), settings.getHashSettings()));
		registerAction(new ConsistencyAction(client, settings.getProcessorSettings(), settings.getConsistencySettings()));
		registerAction(new ChunkHashAction(client, settings.getProcessorSettings(), settings.getHashSettings()));
		registerAction(new SHA256Action(client, settings.getProcessorSettings(), settings.getHashSettings()));
		//registerAction(new MD5Action(client, settings.getProcessorSettings(), settings.getHashSettings()));
		//registerAction(new ThumbnailAction(client, settings.getProcessorSettings(), settings.getThumbnailSettings()));
		//registerAction(new FacedetectAction(client, settings.getProcessorSettings(), settings.getFacedetectActionSettings()));
		registerAction(new FingerprintAction(client, settings.getProcessorSettings(), settings.getFingerprintActionSettings()));
		processor.analyze(folder);
	}

	private void registerAction(FilesystemAction action) {
		processor.registerAction(action);
	}

}
