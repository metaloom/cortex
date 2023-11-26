package io.metaloom.cortex.cli;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.cli.dagger.CortexComponent;
import io.metaloom.cortex.cli.dagger.DaggerCortexComponent;
import picocli.CommandLine;

public class CortexComponentTest {

	@Test
	public void testDaggerSetup() {
		CortexComponent component = DaggerCortexComponent.builder().build();
		Cortex cortex = component.cortex();
		// cortex.checkActions();
		CommandLine cli = component.cli();
		cli.execute("-help");
	}

}
