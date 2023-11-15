package io.metaloom.loom.cortex.action.facedetect.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.metaloom.video.facedetect.Face;
import io.metaloom.video4j.proto.FaceData;
import io.metaloom.video4j.proto.FaceEntry;
import io.metaloom.video4j.proto.Header;

public class FaceFileUtil {

	public static final int FILE_VERSION = 1;

	public static final String FILE_TYPE = "metaloom.facedata";

	public static void writeFaceFile(File file, List<Face> faces) throws FileNotFoundException, IOException {
		Header header = Header.newBuilder().setVersion(FILE_VERSION).setType(FILE_TYPE).build();

		Set<FaceEntry> faceEntries = faces.stream().map(FaceFileUtil::toFaceEntry).collect(Collectors.toSet());
		FaceData data = FaceData.newBuilder().setHeader(header).addAllFaces(faceEntries).build();

		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(data.toByteArray());
		}
	}

	private static FaceEntry toFaceEntry(Face face) {
		FaceEntry.Builder entryBuilder = FaceEntry.newBuilder()
			.setStartX(face.start().x)
			.setStartY(face.start().y)
			.setWidth(face.dimension().width)
			.setHeight(face.dimension().height);

		if (face.label() != null) {
			entryBuilder.setLabel(face.label());
		}
		return entryBuilder.build();
	}

	public static FaceData readFaceData(File file) throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return FaceData.parseFrom(fis);
		}
	}
}
