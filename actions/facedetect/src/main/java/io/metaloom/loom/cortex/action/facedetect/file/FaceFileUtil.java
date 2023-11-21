package io.metaloom.loom.cortex.action.facedetect.file;

import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.primitives.Floats;

import io.metaloom.video.facedetect.Face;
import io.metaloom.video4j.proto.FaceAttribute;
import io.metaloom.video4j.proto.FaceData;
import io.metaloom.video4j.proto.FaceEntry;
import io.metaloom.video4j.proto.Header;

public class FaceFileUtil {

	public static final int FILE_VERSION = 1;

	public static final String FILE_TYPE = "metaloom.facedata";

	public static void writeFaceFile(Path path, List<Face> faces) throws FileNotFoundException, IOException {
		Header header = Header.newBuilder().setVersion(FILE_VERSION).setType(FILE_TYPE).build();

		Set<FaceEntry> faceEntries = faces.stream().map(FaceFileUtil::toFaceEntry).collect(Collectors.toSet());
		FaceData data = FaceData.newBuilder().setHeader(header).addAllFaces(faceEntries).build();

		try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
			fos.write(data.toByteArray());
		}
	}

	private static FaceEntry toFaceEntry(Face face) {
		FaceEntry.Builder entryBuilder = FaceEntry.newBuilder()
			.setStartX(face.start().x)
			.setStartY(face.start().y)
			.setWidth(face.dimension().width)
			.setHeight(face.dimension().height);

		if (face.hasEmbedding()) {
			float[] data = face.getEmbedding();
			entryBuilder.addAllEmbedding(Floats.asList(data));
		}

		if (face.label() != null) {
			entryBuilder.setLabel(face.label());
		}

		for (Entry<String, Object> entry : face.data().entrySet()) {
			FaceAttribute attr = FaceAttribute.newBuilder().setKey(entry.getKey()).setValue(entry.getValue().toString()).build();
			entryBuilder.addAttribute(attr);
		}
		return entryBuilder.build();
	}

	public static FaceData readFaceData(Path path) throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(path.toFile())) {
			return FaceData.parseFrom(fis);
		}
	}

	public static List<Face> readFaceFile(Path path) throws FileNotFoundException, IOException {
		FaceData data = readFaceData(path);
		return data.getFacesList().stream().map(FaceFileUtil::fromFaceEntry).toList();
	}

	public static Face fromFaceEntry(FaceEntry entry) {
		Rectangle box = new Rectangle(entry.getStartX(), entry.getStartY(), entry.getWidth(), entry.getHeight());
		Face face = Face.create(box);
		face.setLabel(entry.getLabel());
		Float[] readEmbedding = entry.getEmbeddingList().toArray(new Float[entry.getEmbeddingCount()]);
		float[] readEmbeddingF = ArrayUtils.toPrimitive(readEmbedding);
		face.setEmbeddings(readEmbeddingF);
		entry.getAttributeList().forEach(attr -> {
			face.data().put(attr.getKey(), attr.getValue());
		});
		return face;
	}
}
