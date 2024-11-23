package io.metaloom.cortex.cli;

import javax.inject.Inject;
import javax.inject.Singleton;

import ch.qos.logback.classic.Level;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;
import picocli.CommandLine.Spec;

@Singleton
@Command(name = "cortex", mixinStandardHelpOptions = false, version = "Cortex 1.0.0-SNAPSHOT", description = "Cortex is a media processing tool", showDefaultValues = true)
public class CortexCLI implements Runnable {

	public static final int DEFAULT_PORT = 7733;
	public static final String DEFAULT_PORT_STR = "7733";
	public static final String DEFAULT_HOSTNAME = "localhost";

	private String hostname = DEFAULT_HOSTNAME;

	private int port = DEFAULT_PORT;

	@Spec
	CommandSpec spec;

	@Inject
	public CortexCLI() {
	}

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

	@Override
	public void run() {
		spec.commandLine().usage(System.out);
	}

}
