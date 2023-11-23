package io.metaloom.cortex.api.option.action;

public class ActionOptions {

	private final FacedetectOptions facedetection = new FacedetectOptions();

	private final FingerprintOptions fingerprint = new FingerprintOptions();

	private final ThumbnailOptions thumbnail = new ThumbnailOptions();

	private final HashOptions hash = new HashOptions();

	public FacedetectOptions getFacedetection() {
		return facedetection;
	}

	public FingerprintOptions getFingerprint() {
		return fingerprint;
	}

	public ThumbnailOptions getThumbnail() {
		return thumbnail;
	}

	public HashOptions getHash() {
		return hash;
	}

}
