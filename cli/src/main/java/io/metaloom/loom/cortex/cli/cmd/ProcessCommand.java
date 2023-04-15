package io.metaloom.loom.cortex.cli.cmd;

import static io.metaloom.loom.cortex.cli.ExitCode.ERROR;
import static io.metaloom.loom.cortex.cli.ExitCode.OK;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.processor.impl.DefaultMediaProcessorImpl;
import io.metaloom.cortex.scanner.FilesystemProcessorSetting;
import picocli.CommandLine.Command;

@Command(name = "process", aliases = { "p" }, description = "Process command")
public class ProcessCommand extends AbstractLoomWorkerCommand {

	public static final Logger log = LoggerFactory.getLogger(ProcessCommand.class);

	@Command(name = "analyze", description = "Analyze the files")
	public int analyze(String path) {
		try {
			Path folder = Paths.get(path);
			FilesystemProcessorSetting settings = new FilesystemProcessorSetting();
			settings.getProcessorSettings().setPort(getPort());
			settings.getProcessorSettings().setHostname(getHostname());
			new DefaultMediaProcessorImpl(settings).process(folder);
			return OK.code();
		} catch (Exception e) {
			log.error("Restoring collections failed.", e);
			return ERROR.code();
		}
	}
}
