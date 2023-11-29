package io.metaloom.cortex.action.scene.detector;

public class Scene {

	public long from = 0;
	public long to = 0;

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
