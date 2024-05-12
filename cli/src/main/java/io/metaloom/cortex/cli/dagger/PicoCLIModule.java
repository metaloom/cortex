package io.metaloom.cortex.cli.dagger;

import dagger.Provides;
import io.metaloom.cortex.cli.CortexCLI;
import io.metaloom.cortex.cli.cmd.ProcessCommand;
import picocli.CommandLine;
import dagger.Module;

@Module
public abstract class PicoCLIModule {

	@Provides
	public static CommandLine commandLine(CortexCLI cli, ProcessCommand processCommand) {
		CommandLine cmd = new CommandLine(cli);
		cmd.addSubcommand("po", processCommand);
		return cmd;
	}
}
