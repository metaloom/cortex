package io.metaloom.loom.cortex.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.metaloom.loom.test.LocalTestData;

public class ProcessCommandTest {

	@BeforeAll
	public static void setup() throws IOException {
		LocalTestData.resetXattr();
	}

	@Test
	public void testCommand() {
		String path = LocalTestData.localDir().toString();
		int code = CortexCLIMain.execute("p", "analyze", path);
		assertEquals(0, code, "The command should not have failed");
	}
}
