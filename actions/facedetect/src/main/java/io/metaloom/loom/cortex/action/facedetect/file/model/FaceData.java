package io.metaloom.loom.cortex.action.facedetect.file.model;

import java.util.ArrayList;
import java.util.List;

import io.metaloom.video.facedetect.face.Face;

public class FaceData {

	private List<Face> entries = new ArrayList<>();

	public List<Face> getEntries() {
		return entries;
	}

	public void setEntries(List<Face> entries) {
		this.entries = entries;
	}

}
