package io.metaloom.loom.worker.cli.cmd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.client.grpc.impl.LoomGRPCClientImpl.Builder;
import io.metaloom.loom.worker.action.consistency.ConsistencyAction;
import io.metaloom.loom.worker.action.facedetect.FacedetectAction;
import io.metaloom.loom.worker.action.thumbnail.ThumbnailAction;
import io.metaloom.loom.worker.processor.FilesystemProcessor;
import io.metaloom.loom.worker.processor.impl.FilesystemProcessorImpl;
import io.metaloom.loom.worker.settings.FilesystemProcessorSetting;
import io.metaloom.worker.action.api.FilesystemAction;
import io.metaloom.worker.action.fp.FingerprintAction;
import io.metaloom.worker.action.hash.ChunkHashAction;
import io.metaloom.worker.action.hash.SHA256Action;
import io.metaloom.worker.action.hash.SHA512Action;

public class DefaultProcessor {

	private final FilesystemProcessor processor;
	private final FilesystemProcessorSetting settings;

	public DefaultProcessor(FilesystemProcessorSetting settings) {
		this.settings = settings;
		this.processor = new FilesystemProcessorImpl();
	}

	public void process(Path folder) throws IOException {
		if (!Files.exists(folder)) {
			throw new FileNotFoundException("Startfolder not found " + folder.toString());
		}

		String hostname = settings.getProcessorSettings().getHostname();
		int port = settings.getProcessorSettings().getPort();
		//TODO configure path
		settings.getThumbnailSettings().setThumbnailPath("target/thumbnails");
		Builder builder = LoomGRPCClient.builder().setHostname(hostname).setPort(port);
		try (LoomGRPCClient client = builder.build()) {
			registerAction(new SHA512Action(client, settings.getProcessorSettings(), settings.getHashSettings()));

			registerAction(new ConsistencyAction(client, settings.getProcessorSettings(), settings.getConsistencySettings()));
			registerAction(new ChunkHashAction(client, settings.getProcessorSettings(), settings.getHashSettings()));
			registerAction(new SHA256Action(client, settings.getProcessorSettings(), settings.getHashSettings()));
			registerAction(new ThumbnailAction(client, settings.getProcessorSettings(), settings.getThumbnailSettings()));
			registerAction(new FacedetectAction(client, settings.getProcessorSettings(), settings.getFacedetectActionSettings()));
			registerAction(new FingerprintAction(client, settings.getProcessorSettings(), settings.getFingerprintActionSettings()));
			processor.analyze(folder);
		}
	}

	private void registerAction(FilesystemAction action) {
		processor.registerAction(action);
	}

}
