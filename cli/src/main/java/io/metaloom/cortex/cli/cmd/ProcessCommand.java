package io.metaloom.cortex.cli.cmd;

import static io.metaloom.cortex.cli.ExitCode.ERROR;
import static io.metaloom.cortex.cli.ExitCode.OK;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.processor.MediaProcessor;
import picocli.CommandLine.Command;

@Singleton
@Command(name = "process", aliases = { "p" }, description = "Process command")
public class ProcessCommand extends AbstractLoomWorkerCommand {

	public static final Logger log = LoggerFactory.getLogger(ProcessCommand.class);

	private final MediaProcessor processor;

	@Inject
	public ProcessCommand(MediaProcessor processor) {
		this.processor = processor;
	}

	@Command(name = "analyze", description = "Analyze the files")
	public int analyze(String path) {
		try {
			Path folder = Paths.get(path);
			processor.process(folder);
			return OK.code();
		} catch (Exception e) {
			log.error("Restoring collections failed.", e);
			return ERROR.code();
		}
	}
}
