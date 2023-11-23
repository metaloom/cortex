package io.metaloom.cortex.action.api.media.param;

public class FaceDetectionParameters implements BSONAttr {

	private int count;

	public FaceDetectionParameters setCount(int count) {
		this.count = count;
		return this;
	}

	public int getCount() {
		return count;
	}

}
