package io.metaloom.worker.action.common.dlib;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

public class DLibModelProvisionerTest {

	@Test
	public void testModelExtraction() throws IOException {
		File destFolder = new File("target", "test-extract");
		if(destFolder.exists()) {
			FileUtils.deleteDirectory(destFolder);
		}
		DLibModelProvisioner.extractModelData(destFolder.toPath());
		assertTrue(new File(destFolder, DLibModelProvisioner.FACE_DETECTOR_MMOD_MODEL).exists());
		assertTrue(new File(destFolder, DLibModelProvisioner.FACE_LANDMARKS_MODEL).exists());
		assertTrue(new File(destFolder, DLibModelProvisioner.FACE_RESNET_MODEL).exists());
	}
}
