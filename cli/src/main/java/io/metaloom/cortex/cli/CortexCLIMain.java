package io.metaloom.cortex.cli;

import io.metaloom.cortex.cli.dagger.CortexComponent;
import io.metaloom.cortex.cli.dagger.DaggerCortexComponent;
import picocli.CommandLine;

public class CortexCLIMain {

	public static void main(String... args) {
		System.exit(execute(args));
	}

	public static int execute(String... args) {
		CortexComponent cortexComponent = DaggerCortexComponent.builder().build();
		CommandLine cli = cortexComponent.cli();
		return cli.execute(args);
	}
}
