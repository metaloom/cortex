package io.metaloom.loom.cortex.action.facedetect.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.undercouch.bson4jackson.BsonFactory;
import io.metaloom.loom.cortex.action.facedetect.file.model.FaceData;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video.facedetect.face.FaceBox;
import io.metaloom.video.facedetect.face.impl.FaceBoxImpl;
import io.metaloom.video.facedetect.face.impl.FaceImpl;

public class FaceFileUtil {

	private static final ObjectMapper MAPPER;

	static {
		MAPPER = new ObjectMapper(new BsonFactory());

		SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
		resolver.addMapping(Face.class, FaceImpl.class);
		resolver.addMapping(FaceBox.class, FaceBoxImpl.class);

		SimpleModule module = new SimpleModule();
		module.setAbstractTypes(resolver);

		MAPPER.registerModule(module);

	}

	public static void writeFaceFile(Path path, FaceData faceData) throws FileNotFoundException, IOException {
		try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
			MAPPER.writeValue(fos, faceData);
		}
	}

	public static FaceData readFaceData(Path path) throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(path.toFile())) {
			return MAPPER.readValue(fis, FaceData.class);
		}
	}

}
