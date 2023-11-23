package io.metaloom.cortex.api.action.media.param;

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
