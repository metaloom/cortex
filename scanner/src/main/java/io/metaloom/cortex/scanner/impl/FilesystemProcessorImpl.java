package io.metaloom.cortex.scanner.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.common.media.impl.LoomMediaImpl;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.action.ResultState;
import io.metaloom.cortex.scanner.FilesystemProcessor;
import io.metaloom.fs.FileInfo;
import io.metaloom.fs.FileState;
import io.metaloom.fs.linux.LinuxFilesystemScanner;
import io.metaloom.fs.linux.impl.LinuxFilesystemScannerImpl;
import io.metaloom.utils.fs.FilterHelper;
import io.metaloom.utils.hash.SHA512;

public class FilesystemProcessorImpl implements FilesystemProcessor {

	private final List<FilesystemAction> actions = new ArrayList<>();

	private final LinuxFilesystemScanner scanner = new LinuxFilesystemScannerImpl();

	public static final Logger log = LoggerFactory.getLogger(FilesystemProcessorImpl.class);

	@Override
	public void analyze(Path path) throws IOException {

		AtomicLong count = new AtomicLong(0);
		// 1. Scan the whole fs tree and count the files.
		List<FileInfo> newMediaFiles = scanner.scanStream(path)
			.filter(info -> {
				return info.state() == FileState.NEW;
			})
			.filter(info -> FilterHelper.isVideo(info.path()))
			.filter(info -> FilterHelper.notEmpty(info.path()))
			.collect(Collectors.toList());

		long total = newMediaFiles.size();

		newMediaFiles.stream().map(info -> new LoomMediaImpl(info.path()))
			.forEach(media -> {
				long current = count.incrementAndGet();
				boolean processed = false;
				System.out.println("[" + media.path() + "]");
				for (FilesystemAction action : actions) {
					action.set(current, total);
					try {
						ActionResult result = action.process(media);
						processed |= result.getState() == ResultState.SUCCESS;
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
				SHA512 hashsum = media.getSHA512();
				log.trace("Adding " + hashsum);
				if (current % 1000 == 0) {
					log.info("Count: " + current);
				}
			});
	}

	@Override
	public void registerAction(FilesystemAction action) {
		actions.add(action);
	}

	@Override
	public LinuxFilesystemScanner getScanner() {
		return scanner;
	}

}
