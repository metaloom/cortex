package io.metaloom.loom.cortex.action.facedetect.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.metaloom.loom.cortex.action.facedetect.file.model.FaceData;
import io.metaloom.utils.fs.FolderUtils;
import io.metaloom.utils.hash.SHA512Sum;

public class FaceDataLocalStorage {

	private final Path basePath;

	public FaceDataLocalStorage(Path basePath) {
		this.basePath = basePath;
	}

	public Path store(SHA512Sum hash, FaceData faceData) throws IOException {
		Path filePath = toFilePath(basePath, hash);
		Path dirPath = filePath.getParent();
		if (!Files.exists(dirPath)) {
			Files.createDirectories(dirPath);
		}
		FaceFileUtil.writeFaceFile(filePath, faceData);
		return filePath;
	}

	public FaceData load(SHA512Sum hash) throws IOException {
		Path filePath = toFilePath(basePath, hash);
		if (!Files.exists(filePath)) {
			return null;
		}
		return FaceFileUtil.readFaceData(filePath);
	}

	public static Path toFilePath(Path basePath, SHA512Sum hash) throws IOException {
		Path dirPath = FolderUtils.segmentPath(basePath, hash);
		return dirPath.resolve(Paths.get(hash.toString() + ".face"));
	}
}
