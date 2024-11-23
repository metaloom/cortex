package io.metaloom.cortex.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.loom.test.LocalTestData;

public class ProcessCommandTest extends AbstractCortexTest {

	@BeforeAll
	public static void setup() throws IOException {
		LocalTestData.resetXattr();
	}

	@Test
	public void testCommand() {
		String path = LocalTestData.localDir().toString();
		CortexOptions defaultOptions = createOptions();

		int code = CortexCLIMain.execute(defaultOptions, "p", "run", "-a" , "sha512,llm", path);
		assertEquals(0, code, "The command should not have failed");
	}

}
