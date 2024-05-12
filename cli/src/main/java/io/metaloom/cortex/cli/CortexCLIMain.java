package io.metaloom.cortex.cli;

import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.cli.dagger.CortexComponent;
import io.metaloom.cortex.cli.dagger.DaggerCortexComponent;
import picocli.CommandLine;

public class CortexCLIMain {

	public static void main(String... args) {
		System.exit(execute(null, args));
	}

	public static int execute(CortexOptions options, String... args) {
		CortexComponent.Builder builder = DaggerCortexComponent.builder();
		builder.options(options);
		CortexComponent cortexComponent = builder.build();
		CommandLine cli = cortexComponent.cli();
		return cli.execute(args);
	}
}
