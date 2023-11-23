package io.metaloom.cortex.common.option;

import static io.metaloom.cortex.CortexEnv.CORTEX_CONF_FILENAME;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.metaloom.cortex.api.option.CortexOptions;

public final class CortexOptionsLoader {

	private static final Logger log = LoggerFactory.getLogger(CortexOptionsLoader.class);

	private CortexOptionsLoader() {

	}

	/**
	 * Return the preconfigured object mapper which is used to transform YAML documents.
	 * 
	 * @return
	 */
	public static ObjectMapper getYAMLMapper() {
		YAMLFactory factory = new YAMLFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(Include.ALWAYS);
		return mapper;
	}

	public static CortexOptions createOrLoadOptions() {
		CortexOptions options = loadCortexOptions();
		// applyNonYamlProperties(defaultOption, options);
		// applyEnvironmentVariables(options);
		// applyCommandLineArgs(options, args);
		// options.validate();
		return options;
	}

	/**
	 * Try to load the loom options from different locations (classpath, config folder). Otherwise a default configuration will be generated.
	 * 
	 * @param defaultOption
	 * 
	 * @return
	 */
	private static CortexOptions loadCortexOptions() {
		String home = System.getProperty("user.home");
		Path configDir = Paths.get(home, ".config", "metaloom");
		Path configFile = configDir.resolve(CORTEX_CONF_FILENAME);

		CortexOptions options = null;

		// 1. Try to use config file
		if (Files.exists(configFile)) {
			try {
				log.info("Loading configuration file {" + configFile + "}.");
				try (FileInputStream fis = new FileInputStream(configFile.toFile())) {
					options = loadConfiguration(fis);
					if (options != null) {
						return options;
					}
				}
			} catch (IOException e) {
				log.error("Could not load configuration file {" + configFile.toAbsolutePath() + "}.", e);
			}
		} else {
			log.info("Configuration file {" + configFile.toAbsolutePath().toString() + "} was not found within the filesystem.");

			ObjectMapper mapper = getYAMLMapper();
			try {
				// Generate default config
				options = generateDefaultConfig();
				FileUtils.writeStringToFile(configFile.toFile(), mapper.writeValueAsString(options), StandardCharsets.UTF_8, false);
				log.info("Saved default configuration to file {" + configFile.toAbsolutePath() + "}.");
			} catch (IOException e) {
				log.error("Error while saving default configuration to file {" + configFile.toAbsolutePath() + "}.", e);
			}
		}
		// 2. No luck - use default config
		log.info("Loading default configuration.");
		return options;

	}

	/**
	 * Load the configuration from the stream.
	 * 
	 * @param ins
	 * @return
	 */
	private static CortexOptions loadConfiguration(InputStream ins) {
		if (ins == null) {
			log.info("Config file {" + CORTEX_CONF_FILENAME + "} not found. Using default configuration.");
			return generateDefaultConfig();
		}

		ObjectMapper mapper = getYAMLMapper();
		try {
			return mapper.readValue(ins, CortexOptions.class);
		} catch (Exception e) {
			log.error("Could not parse configuration.", e);
			throw new RuntimeException("Could not parse options file", e);
		}
	}

	/**
	 * Generate a default configuration with meaningful default settings.
	 * 
	 * @return
	 */
	public static CortexOptions generateDefaultConfig() {
		CortexOptions options = new CortexOptions();
		return options;
	}

}
