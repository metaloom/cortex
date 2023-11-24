package io.metaloom.loom.cortex.cli;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.api.option.action.CortexActionOptions;
import io.metaloom.loom.cortex.cli.dagger.CortexComponent;
import io.metaloom.loom.cortex.cli.dagger.DaggerCortexComponent;

public class CortexComponentTest {

	@Test
	public void testDaggerSetup() {
		CortexComponent component = DaggerCortexComponent.builder().build();
		Cortex cortex = component.cortex();
	}

	@Test
	public void testLoadConfig() throws JsonProcessingException {
		CortexComponent component = DaggerCortexComponent.builder().build();
		CortexOptions options = component.options();
		CortexActionOptions o = options.getActions().get("thumbnail");
		assertNotNull(o);

		String str = component.optionsLoader().getYAMLMapper().writeValueAsString(options);
		System.out.println(str);
	}

}
