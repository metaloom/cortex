package io.metaloom.cortex.api.media.param;

public class DetectedScene {

	public long from = 0;
	public long to = 0;

	public DetectedScene(long from, long to) {
		this.from = from;
		this.to = to;
	}

	public long getFrom() {
		return from;
	}

	public long getTo() {
		return to;
	}

	public long length() {
		return to - from;
	}

}
