package io.metaloom.cortex.api.media.param;

public class FaceDetectionParameter implements BSONAttr {

	private int count;

	public FaceDetectionParameter setCount(int count) {
		this.count = count;
		return this;
	}

	public int getCount() {
		return count;
	}

}
