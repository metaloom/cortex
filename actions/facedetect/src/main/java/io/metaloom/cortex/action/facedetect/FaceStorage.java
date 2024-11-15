//package io.metaloom.cortex.action.facedetect;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.util.List;
//import java.util.Objects;
//
//import io.metaloom.cortex.action.facedetect.file.model.FaceData;
//import io.metaloom.cortex.common.avro.AbstractAvroStorage;
//import io.metaloom.loom.cortex.action.facedetect.avro.Facedetection;
//import io.metaloom.loom.cortex.action.facedetect.avro.FacedetectionBox;
//import io.metaloom.utils.FloatUtils;
//import io.metaloom.utils.hash.SHA512;
//import io.metaloom.video.facedetect.face.Face;
//import io.metaloom.video.facedetect.face.FaceBox;
//import io.metaloom.video.facedetect.face.impl.FaceBoxImpl;
//import io.metaloom.video.facedetect.face.impl.FaceImpl;
//
//public class FaceStorage extends AbstractAvroStorage<Facedetection> {
//
//	private final File baseDir;
//
//	public FaceStorage(Path basePath) {
//		Objects.requireNonNull(basePath);
//		this.baseDir = basePath.toFile();
//		this.baseDir.mkdirs();
//	}
//
//	public void store(Facedetection data) throws IOException {
////		SHA512 hash = data.getAssetHash();
////		List<Facedetection> avroElements = data.getEntries().stream().map(entry -> {
////			int nFrame = 0;
////			List<Float> embedding = FloatUtils.toList(entry.getEmbedding());
////			FacedetectionBox box = toFacedetectionBox(entry.box());
////			Facedetection fd = toFacedetection(hash, nFrame, box, embedding);
////			fd.setAssetHash(hash.toString());
////			return fd;
////		}).toList();
//		//writeElements(hash, data);
//
//	}
//
//	private Facedetection toFacedetection(SHA512 hash, int nFrame, FacedetectionBox box, List<Float> embedding) {
//		return new Facedetection(hash.toString(), nFrame, box, embedding);
//	}
//
//	private FacedetectionBox toFacedetectionBox(FaceBox box) {
//		return new FacedetectionBox(box.getStartX(), box.getStartY(), box.getHeight(), box.getWidth());
//	}
//
//	@Override
//	protected File getFileForHash(SHA512 hash) {
//		return new File(baseDir, hash.toString() + ".avro");
//	}
//
//	@Override
//	protected Class<Facedetection> getElementClass() {
//		return Facedetection.class;
//	}
//
//	public FaceData read(SHA512 hash) throws IOException {
//		List<Facedetection> avroDetections = readElements(hash);
//
//		FaceData data = new FaceData(hash);
//		List<Face> list = avroDetections.stream().map(entry -> {
//			Face face = new FaceImpl();
//			face.setBox(toFaceBox(entry.getBox()));
//			face.setEmbedding(FloatUtils.toArray(entry.getEmbedding()));
//			return face;
//		}).toList();
//		data.setEntries(list);
//		return data;
//	}
//
//	private FaceBox toFaceBox(FacedetectionBox box) {
//		FaceBox faceBox = new FaceBoxImpl();
//		faceBox.setStartX(box.getStartX());
//		faceBox.setStartY(box.getStartY());
//		faceBox.setHeight(box.getHeight());
//		faceBox.setWidth(box.getWidth());
//		return faceBox;
//	}
//
//}
