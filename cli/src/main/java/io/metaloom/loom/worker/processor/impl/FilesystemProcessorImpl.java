package io.metaloom.loom.worker.processor.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.loom.worker.processor.FilesystemProcessor;
import io.metaloom.utils.fs.FilterHelper;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.FilesystemAction;
import io.metaloom.worker.action.api.ResultState;
import io.metaloom.worker.action.common.media.ProcessableMediaImpl;

public class FilesystemProcessorImpl implements FilesystemProcessor {

	private final List<FilesystemAction> actions = new ArrayList<>();

	public static final Logger log = LoggerFactory.getLogger(FilesystemProcessorImpl.class);

	@Override
	public void analyze(Path path) throws IOException {
		AtomicLong count = new AtomicLong(0);
		// 1. Scan the whole fs tree and count the files.
		long total = stream(path).count();
		stream(path)
			.map(ProcessableMediaImpl::new)
			.forEach(media -> {
				long current = count.incrementAndGet();
				boolean processed = false;
				System.out.println("[" + media.path() + "]");
				for (FilesystemAction action : actions) {
					action.set(current, total);
					try {
						ActionResult result = action.process(media);
						processed |= result.getState() == ResultState.PROCESSED;
						if (!result.isContinueNext()) {
							action.error(media, "Aborting further processing");
							// Abort further processing
							return;
						}
					} catch (Exception e) {
						action.error(media, "Error while processing action " + action.name());
						e.printStackTrace();
					}
					if (current % 100 == 0) {
						action.flush();
					}
				}
				if (processed) {
					System.out.println();
				}
				String hashsum = media.getHash512();
				log.trace("Adding " + hashsum);
				if (current % 1000 == 0) {
					log.info("Count: " + current);
				}

			});
	}

	private Stream<Path> stream(Path path) throws IOException {
		return Files.walk(path)
			.filter(Files::isRegularFile)
			.filter(FilterHelper::isVideo)
			.filter(FilterHelper::notEmpty);
	}

	@Override
	public void registerAction(FilesystemAction action) {
		actions.add(action);
	}

}
