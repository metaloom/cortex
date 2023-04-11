package io.metaloom.loom.worker.cli.cmd;

import static io.metaloom.loom.worker.cli.ExitCode.OK;

import io.metaloom.loom.worker.cli.LoomWorkerCLI;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Spec;

@Command
public class AbstractLoomWorkerCommand implements LoomWorkerCommand {

	@Spec
	CommandSpec spec;

	@ParentCommand
	private LoomWorkerCLI parent;

	@Override
	public Integer call() {
		spec.commandLine().usage(System.out);
		return OK.code();
	}

	@Override
	public String getHostname() {
		return parent.getHostname();
	}

	@Override
	public int getPort() {
		return parent.getPort();
	}
}
