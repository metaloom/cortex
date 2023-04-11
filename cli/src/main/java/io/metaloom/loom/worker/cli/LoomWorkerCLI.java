package io.metaloom.loom.worker.cli;

import ch.qos.logback.classic.Level;
import io.metaloom.loom.worker.cli.cmd.ProcessCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;
import picocli.CommandLine.Spec;


@Command(name = "loom-worker", mixinStandardHelpOptions = false, version = "loom-worker 0.0.1", description = "CLI tool for the Loom DAM", showDefaultValues = true, subcommands = {
	ProcessCommand.class
})
public class LoomWorkerCLI implements Runnable {

	private static LoomWorkerCLI instance = new LoomWorkerCLI();

	public static final int DEFAULT_PORT = 7733;
	public static final String DEFAULT_PORT_STR = "7733";
	public static final String DEFAULT_HOSTNAME = "localhost";

	private String hostname = DEFAULT_HOSTNAME;

	private int port = DEFAULT_PORT;

	@Spec
	CommandSpec spec;

	@Option(names = "-v", scope = ScopeType.INHERIT)
	public void setVerbose(boolean[] verbose) {
		Level level = Level.INFO;
		if (verbose.length > 0) {
			level = Level.DEBUG;
		}
		if (verbose.length >= 1) {
			level = Level.TRACE;
		}
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory
			.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
		root.setLevel(level);
	}

	public String getHostname() {
		return hostname;
	}

	@Option(names = { "-h", "--hostname" }, description = "Hostname to connect to", defaultValue = DEFAULT_HOSTNAME, scope = ScopeType.INHERIT)
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	@Option(names = { "-p", "--port" }, description = "HTTP port to connect to.", defaultValue = DEFAULT_PORT_STR, scope = ScopeType.INHERIT)
	public void setPort(int port) {
		this.port = port;
	}

	public static void main(String... args) {
		System.exit(execute(args));
	}

	public static int execute(String... args) {
		CommandLine cmd = new CommandLine(LoomWorkerCLI.instance());
		return cmd.execute(args);
	}

	@Override
	public void run() {
		spec.commandLine().usage(System.out);
	}

	public static LoomWorkerCLI instance() {
		return instance;
	}
}
