package io.metaloom.cortex.scanner.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.action.ResultState;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.context.impl.ActionContextImpl;
import io.metaloom.cortex.common.media.LoomMediaLoader;
import io.metaloom.cortex.scanner.FilesystemProcessor;
import io.metaloom.fs.FileInfo;
import io.metaloom.fs.FileState;
import io.metaloom.fs.linux.LinuxFilesystemScanner;
import io.metaloom.utils.fs.FilterHelper;
import io.metaloom.utils.hash.SHA512;
import me.tongfei.progressbar.ProgressBar;

@Singleton
public class FilesystemProcessorImpl implements FilesystemProcessor {

	private final Set<FilesystemAction> actions;

	private final LinuxFilesystemScanner scanner;

	private final LoomMediaLoader loader;

	public static final Logger log = LoggerFactory.getLogger(FilesystemProcessorImpl.class);

	@Inject
	public FilesystemProcessorImpl(LinuxFilesystemScanner scanner, Set<FilesystemAction> actions, LoomMediaLoader loader) {
		this.scanner = scanner;
		this.actions = actions;
		this.loader = loader;
	}

	@Override
	public void analyze(List<String> enabledActions, Path path) throws IOException {

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
		try (ProgressBar pb = new ProgressBar("Processor", total)) {

			newMediaFiles.stream().map(info -> loader.load(info.path()))
				.forEach(media -> {

					long current = count.incrementAndGet();
					boolean processed = false;
					// pb.setExtraMessage("[" + media.path().toFile().getName() + "]");
					for (FilesystemAction<?> action : actions) {
						if (enabledActions != null && !enabledActions.isEmpty() && !enabledActions.contains(action.name().toLowerCase())) {
							if (log.isDebugEnabled()) {
								log.debug("Action {} will be skipped", action.name());
							}
							continue;
						}

						log.debug("Processing media {} using action {}", media, action.name());
						action.set(current, total);
						try {
							ActionContext ctx = new ActionContextImpl(media);
							ActionResult result = action.process(ctx);
							if (result == null) {
								log.error("Action '{}' failed to process media {}. Invalid result returned.", action.name(), media);
								return;
							}
							action.print(ctx, "ok", "msg");
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
					log.trace("Adding {}", hashsum);
					if (current % 1000 == 0) {
						log.info("Count: " + current);
					}
					pb.step();
					pb.refresh();
				});
		}
	}

	@Override
	public LinuxFilesystemScanner getScanner() {
		return scanner;
	}

}
