package io.metaloom.cortex.action.facedetect.video;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video.facedetect.face.FaceBox;

public class VideoFace implements Face {

	public static final String BLURRINESS_KEY = "blurriness";

	public static final String IMAGE_KEY = "image";

	public static final String FRAME_KEY = "frame";

	private Face face;

	public VideoFace(Face face) {
		this.face = face;
	}

	@Override
	public String label() {
		return face.label();
	}

	@Override
	public Face setLabel(String label) {
		face.setLabel(label);
		return this;
	}

	@Override
	public Point start() {
		return face.start();
	}

	@Override
	public Dimension dimension() {
		return face.dimension();
	}

	@Override
	public FaceBox box() {
		return face.box();
	}

	@Override
	public Face setBox(FaceBox box) {
		return this;
	}

	@Override
	public Face setLandmarks(List<Point> facialLandmarks) {
		face.setLandmarks(facialLandmarks);
		return this;
	}

	@Override
	public List<Point> getLandmarks() {
		return face.getLandmarks();
	}

	@Override
	public Face setEmbedding(float[] faceEmbedding) {
		face.setEmbedding(faceEmbedding);
		return this;
	}

	@Override
	public float[] getEmbedding() {
		return face.getEmbedding();
	}

	@Override
	public boolean hasLandmarks() {
		return face.hasLandmarks();
	}

	@Override
	public boolean hasEmbedding() {
		return face.hasEmbedding();
	}

	@Override
	public Map<String, Object> data() {
		return face.data();
	}

	@Override
	public Face setData(Map<String, Object> data) {
		face.setData(data);
		return this;
	}

	public VideoFace setBlurriness(double blurriness) {
		face.set(BLURRINESS_KEY, blurriness);
		return this;
	}

	public double getBlurriness() {
		return face.get(BLURRINESS_KEY);
	}

	public VideoFace setImage(BufferedImage croppedFaceImage) {
		face.set(IMAGE_KEY, croppedFaceImage);
		return this;
	}

	public BufferedImage getImage() {
		return face.get(IMAGE_KEY);
	}

	public VideoFace setFrame(long number) {
		face.set(FRAME_KEY, number);
		return this;
	}

	public Long getFrame() {
		return face.get(FRAME_KEY);
	}

}
