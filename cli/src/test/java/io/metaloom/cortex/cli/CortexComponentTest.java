package io.metaloom.cortex.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.Cortex;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.cli.dagger.CortexComponent;
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
	
	@Test
	public void testSubCompoentHandling() {
		CortexComponent component = DaggerCortexComponent.builder().build();
		LoomMedia media = component.loader().load(Paths.get("target/test"));
		assertNotNull(media);
		assertEquals("target/test", media.path().toString());
	}

}
