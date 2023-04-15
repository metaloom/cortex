package io.metaloom.loom.cortex.cli;

import org.junit.jupiter.api.Test;

import io.metaloom.loom.cortex.cli.cmd.ProcessCommand;
import io.metaloom.loom.test.LocalTestData;

public class ProcessCommandTest {

	@Test
	public void testCommand() {
		ProcessCommand cmd = new ProcessCommand();
		cmd.analyze(LocalTestData.localDir().toString());
	}
}
