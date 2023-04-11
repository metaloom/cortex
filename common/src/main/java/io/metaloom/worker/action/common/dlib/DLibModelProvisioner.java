package io.metaloom.worker.action.common.dlib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DLibModelProvisioner {

	public static final Logger log = LoggerFactory.getLogger(DLibModelProvisioner.class);

	public static final String FACE_RESNET_MODEL = "dlib_face_recognition_resnet_model_v1.dat";
	public static final String FACE_DETECTOR_MMOD_MODEL = "mmod_human_face_detector.dat";
	public static final String FACE_LANDMARKS_MODEL = "shape_predictor_68_face_landmarks.dat";
	public static final String RESOURCE_FOLDER = "dlib";

	private DLibModelProvisioner() {
	}

	public static void extractModelData(Path destination) throws IOException {
		extractResource(FACE_RESNET_MODEL, destination);
		extractResource(FACE_DETECTOR_MMOD_MODEL, destination);
		extractResource(FACE_LANDMARKS_MODEL, destination);
	}

	private static void extractResource(String resourceName, Path destination) throws IOException {
		String resourcePath = "/" + RESOURCE_FOLDER + "/" + resourceName;
		File targetFolder = destination.toFile();
		File targetFile = new File(targetFolder, resourceName);
		if (targetFile.exists()) {
			if (log.isDebugEnabled()) {
				log.debug("Resource {} already extracted to {}. Skipping", resourceName, destination);
			}
			return;
		}
		if (!targetFolder.exists() && !targetFolder.mkdirs()) {
			throw new IOException("Unable to create destination folder " + targetFolder.getAbsolutePath());
		}
		if (log.isDebugEnabled()) {
			log.debug("Extracting {} to {}", resourceName, destination);
		}
		try (InputStream inputStream = DLibModelProvisioner.class.getResourceAsStream(resourcePath)) {
			if (inputStream == null) {
				throw new IOException("Resource " + resourcePath + " not found in classpath.");
			}
			FileUtils.copyInputStreamToFile(inputStream, targetFile);
		}

	}
}
