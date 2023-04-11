package io.metaloom.loom.worker.action.facedetect;

import io.metaloom.worker.action.api.setting.ActionSettings;

public class FacedetectActionSettings implements ActionSettings {

	/**
	 * Process only every nth video frame.
	 */
	private int videoChopRate = 5;

	/**
	 * Defines the minimum of detections that may form a dedicated cluster.
	 */
	public int faceClusterMinimum = 2;

	/**
	 * Defines the minimum radius that is being utilized to cluster faces together.
	 */
	public float faceClusterEPS = 0.6f;

	/**
	 * Defines the size to which every frame will be increased in either width or height before processing. Higher resolution increased detection precision but
	 * also detection time.
	 */
	private int videoScaleSize = 384;

	/**
	 * Defines the height factor in respect to the total frame height which controls whether a found face will be processed further.
	 */
	private float minFaceHeightFactor = 0.05f;

	public int getVideoChopRate() {
		return videoChopRate;
	}

	public FacedetectActionSettings setVideoChopRate(int videoChopRate) {
		this.videoChopRate = videoChopRate;
		return this;
	}

	public int getVideoScaleSize() {
		return videoScaleSize;
	}

	public FacedetectActionSettings setVideoScaleSize(int videoScaleSize) {
		this.videoScaleSize = videoScaleSize;
		return this;
	}

	public int getFaceClusterMinimum() {
		return faceClusterMinimum;
	}

	public FacedetectActionSettings setFaceClusterMinimum(int faceClusterMinimum) {
		this.faceClusterMinimum = faceClusterMinimum;
		return this;
	}

	public float getFaceClusterEPS() {
		return faceClusterEPS;
	}

	public FacedetectActionSettings setFaceClusterEPS(float faceClusterEPS) {
		this.faceClusterEPS = faceClusterEPS;
		return this;
	}

	public float getMinFaceHeightFactor() {
		return minFaceHeightFactor;
	}

	public FacedetectActionSettings setMinFaceHeightFactor(float minFaceHeightFactor) {
		this.minFaceHeightFactor = minFaceHeightFactor;
		return this;
	}
}
