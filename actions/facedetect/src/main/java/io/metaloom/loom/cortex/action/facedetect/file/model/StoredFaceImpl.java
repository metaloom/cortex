//package io.metaloom.loom.cortex.action.facedetect.file.model;
//
//import java.awt.Point;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import io.metaloom.video.facedetect.face.Face;
//import io.metaloom.video.facedetect.face.FaceBox;
//
//public class StoredFaceImpl implements Face {
//
//	private String label;
//	private float[] embedding;
//	private FaceBox box;
//	private Map<String, Object> data;
//	private List<Point> landmarks = new ArrayList<>();
//
//	public StoredFaceImpl() {
//	}
//
//	@Override
//	public String label() {
//		return label;
//	}
//
//	@Override
//	public Face setLabel(String label) {
//		this.label = label;
//		return this;
//	}
//
//	@Override
//	public FaceBox box() {
//		return box;
//	}
//
//	@Override
//	public Face setBox(FaceBox box) {
//		this.box = box;
//		return this;
//	}
//
//	@Override
//	public float[] getEmbedding() {
//		return embedding;
//	}
//
//	@Override
//	public Face setEmbedding(float[] embedding) {
//		this.embedding = embedding;
//		return this;
//	}
//
//	@Override
//	public List<Point> getLandmarks() {
//		return landmarks;
//	}
//
//	@Override
//	public Face setLandmarks(List<Point> facialLandmarks) {
//		this.landmarks = facialLandmarks;
//		return this;
//	}
//
//	@Override
//	public Face setData(Map<String, Object> data) {
//		this.data = data;
//		return this;
//	}
//
//	@Override
//	public Map<String, Object> data() {
//		return data;
//	}
//}
