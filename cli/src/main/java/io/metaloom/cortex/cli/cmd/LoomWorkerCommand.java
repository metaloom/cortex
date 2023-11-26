package io.metaloom.cortex.cli.cmd;

import java.util.concurrent.Callable;

public interface LoomWorkerCommand extends Callable<Integer> {

	int getPort();

	String getHostname();
}
