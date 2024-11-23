package io.metaloom.cortex.cli.cmd;

import static io.metaloom.cortex.cli.ExitCode.ERROR;
import static io.metaloom.cortex.cli.ExitCode.OK;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.processor.MediaProcessor;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Singleton
@Command(name = "process", aliases = { "p" }, description = "Process command")
public class ProcessCommand extends AbstractLoomWorkerCommand {

	public static final Logger log = LoggerFactory.getLogger(ProcessCommand.class);

	private final MediaProcessor processor;

	@Inject
	public ProcessCommand(MediaProcessor processor) {
		this.processor = processor;
	}

	@Command(name = "run", description = "Process the files using the configured actions")
	public int run(
		@Option(names = { "-a", "--actions" }, description = "Actions to be used when processing files.") String enabledActions,
		@Parameters(index = "0", description = "Path to be processed") String path) {
		try {
			Path folder = Paths.get(path);
			String[] actions = enabledActions.split(",");
			processor.process(List.of(actions), folder);
			return OK.code();
		} catch (Exception e) {
			log.error("Restoring collections failed.", e);
			return ERROR.code();
		}
	}

}
