package io.metaloom.cortex.option;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.CortexEnv;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.option.CortexOptionsLoader;

public class CortexOptionsLoaderTest {

	private static Path CONF_PATH = Paths.get("target", "fakehome", ".config", "metaloom", CortexEnv.CORTEX_CONF_FILENAME);

	@BeforeEach
	public void deleteConfig() throws IOException {
		if (Files.exists(CONF_PATH)) {
			Files.delete(CONF_PATH);
		}
	}

	@Test
	public void testLoadOptions() {
		assertFalse(Files.exists(CONF_PATH), "The config file should not yet been there.");
		System.setProperty("user.home", "target/fakehome");
		CortexOptions options = CortexOptionsLoader.createOrLoadOptions();
		assertNotNull(options);
		assertTrue(Files.exists(CONF_PATH), "The config file should have been written");
	}
}
