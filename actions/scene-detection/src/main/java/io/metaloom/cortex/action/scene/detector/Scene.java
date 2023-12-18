package io.metaloom.cortex.action.scene.detector;

public class Scene {

	private long from = 0;
	private long to = 0;

	public Scene(long from, long to) {
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
