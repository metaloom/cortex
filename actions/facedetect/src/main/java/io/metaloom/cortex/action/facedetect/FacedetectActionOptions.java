package io.metaloom.cortex.action.facedetect;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class FacedetectActionOptions extends AbstractActionOptions<FacedetectActionOptions> {

	public static final String KEY = "facedetection";

	@Override
	protected FacedetectActionOptions self() {
		return this;
	}

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

	public FacedetectActionOptions setVideoChopRate(int videoChopRate) {
		this.videoChopRate = videoChopRate;
		return this;
	}

	public int getVideoScaleSize() {
		return videoScaleSize;
	}

	public FacedetectActionOptions setVideoScaleSize(int videoScaleSize) {
		this.videoScaleSize = videoScaleSize;
		return this;
	}

	public int getFaceClusterMinimum() {
		return faceClusterMinimum;
	}

	public FacedetectActionOptions setFaceClusterMinimum(int faceClusterMinimum) {
		this.faceClusterMinimum = faceClusterMinimum;
		return this;
	}

	public float getFaceClusterEPS() {
		return faceClusterEPS;
	}

	public FacedetectActionOptions setFaceClusterEPS(float faceClusterEPS) {
		this.faceClusterEPS = faceClusterEPS;
		return this;
	}

	public float getMinFaceHeightFactor() {
		return minFaceHeightFactor;
	}

	public FacedetectActionOptions setMinFaceHeightFactor(float minFaceHeightFactor) {
		this.minFaceHeightFactor = minFaceHeightFactor;
		return this;
	}
}
