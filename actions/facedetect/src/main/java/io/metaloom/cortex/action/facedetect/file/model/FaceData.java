package io.metaloom.cortex.action.facedetect.file.model;

import java.util.ArrayList;
import java.util.List;

import io.metaloom.utils.hash.SHA512;
import io.metaloom.video.facedetect.face.Face;

public class FaceData {

	private SHA512 assetHash;

	private List<Face> entries = new ArrayList<>();

	public FaceData(SHA512 assetHash) {
		this.assetHash = assetHash;
	}

	public List<Face> getEntries() {
		return entries;
	}

	public FaceData setEntries(List<Face> entries) {
		this.entries = entries;
		return this;
	}

	public SHA512 getAssetHash() {
		return assetHash;
	}

}
