package io.metaloom.loom.cortex.cli;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.loom.cortex.cli.dagger.CortexComponent;
import io.metaloom.loom.cortex.cli.dagger.DaggerCortexComponent;

public class CortexComponentTest {

	@Test
	public void testDaggerSetup() {
		CortexOptions options = new CortexOptions();
		CortexComponent component = DaggerCortexComponent.builder().configuration(options).build();

		Cortex cortex = component.cortex();
	}
}
