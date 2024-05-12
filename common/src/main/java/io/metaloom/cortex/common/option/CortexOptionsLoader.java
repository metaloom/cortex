package io.metaloom.cortex.common.option;

import static io.metaloom.cortex.CortexEnv.CORTEX_CONF_FILENAME;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.api.option.action.CortexActionOptions;

@Singleton
public class CortexOptionsLoader {

	private static final Logger log = LoggerFactory.getLogger(CortexOptionsLoader.class);

	private final CortexActionOptionDeserializer actionOptionsDeserializer;

	@Inject
	public CortexOptionsLoader(CortexActionOptionDeserializer deserializer) {
		this.actionOptionsDeserializer = deserializer;
	}

	/**
	 * Return the preconfigured object mapper which is used to transform YAML documents.
	 * 
	 * @return
	 */
	public ObjectMapper getMapper() {
		YAMLFactory factory = new YAMLFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(Include.ALWAYS);

		SimpleModule module = new SimpleModule();
		module.addDeserializer(CortexActionOptions.class, actionOptionsDeserializer);

		mapper.registerModule(module);
		return mapper;
	}

	// public CortexOptions createOrLoadOptions() {
	// CortexOptions options = loadCortexOptions(defaultConfigPath());
	// // applyNonYamlProperties(defaultOption, options);
	// // applyEnvironmentVariables(options);
	// // applyCommandLineArgs(options, args);
	// // options.validate();
	// return options;
	// }

	public Path defaultConfigPath() {
		String home = System.getProperty("user.home");
		Path configDir = Paths.get(home, ".config", "metaloom");
		Path configFile = configDir.resolve(CORTEX_CONF_FILENAME);
		return configFile;
	}

	/**
	 * Try to load the loom options from different locations (classpath, config folder). Otherwise a default configuration will be generated.
	 * 
	 * @param defaultOption
	 * 
	 * @return
	 */
	public CortexOptions load() {

		// 1. Try to use config file
		CortexOptions options = load(defaultConfigPath());

		// 2. No luck - use default config
		if (options == null) {
			log.info("Loading default configuration.");
			options = generateDefaultConfig();
		}

		validateOptions(options);
		return options;

	}

	private void validateOptions(CortexOptions options) {
		Objects.requireNonNull(options.getMetaPath(), "The metaPath must be specified");
	}

	public CortexOptions load(Path configFile) {
		if (Files.exists(configFile)) {
			try {
				log.info("Loading configuration file {" + configFile + "}.");
				try (FileInputStream fis = new FileInputStream(configFile.toFile())) {
					return load(fis);
				}
			} catch (IOException e) {
				log.error("Could not load configuration file {" + configFile.toAbsolutePath() + "}.", e);

			}
		}
		return null;
	}

	/**
	 * Load the configuration from the stream.
	 * 
	 * @param ins
	 * @return
	 */
	private CortexOptions load(InputStream ins) {
		if (ins == null) {
			log.info("Config file {" + CORTEX_CONF_FILENAME + "} not found. Using default configuration.");
			return generateDefaultConfig();
		}

		ObjectMapper mapper = getMapper();
		try {
			return mapper.readValue(ins, CortexOptions.class);
		} catch (Exception e) {
			log.error("Could not parse configuration.", e);
			throw new RuntimeException("Could not parse options file", e);
		}
	}

	public void save(CortexOptions options) {
		save(defaultConfigPath(), options);
	}

	public void save(Path configFile, CortexOptions options) {
		log.info("Configuration file {" + configFile.toAbsolutePath().toString() + "} was not found within the filesystem.");

		ObjectMapper mapper = getMapper();
		try {
			// Generate default config
			FileUtils.writeStringToFile(configFile.toFile(), mapper.writeValueAsString(options), StandardCharsets.UTF_8, false);
			log.info("Saved default configuration to file {" + configFile.toAbsolutePath() + "}.");
		} catch (IOException e) {
			log.error("Error while saving default configuration to file {" + configFile.toAbsolutePath() + "}.", e);
		}

	}

	/**
	 * Generate a default configuration with meaningful default settings.
	 * 
	 * @return
	 */
	public CortexOptions generateDefaultConfig() {
		CortexOptions options = new CortexOptions();
		return options;
	}

}
