package io.metaloom.loom.worker.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.metaloom.utils.fs.XAttrUtils;

public class LocalTestData {

	public static Path localDir() {
		return Paths.get("/opt/metaloom/loom-testdata");
	}

	public static Path thumbnailDir() {
		return Paths.get("/opt/metaloom/loom-thumbnaildir");
	}

	public static void resetXattr() throws IOException {
		Files.walk(localDir())
			.filter(Files::isRegularFile)
			.forEach(path -> XAttrUtils.clearXAttr(path));
	}
}
