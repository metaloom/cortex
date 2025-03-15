package io.metaloom.cortex.action.facedetect.video;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import io.metaloom.video.facedetect.face.Face;

public class VideoFaceScannerReport {

	private long start = System.currentTimeMillis();

	private Duration duration;

	private List<Face> faces = new ArrayList<>();

	private int totalWindowCount;

	private int locatedWindowCount;

	public List<Face> getFaces() {
		return faces;
	}

	public void setFaces(List<Face> faces) {
		this.duration = Duration.ofMillis(System.currentTimeMillis() - start);
		this.faces = faces;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setWindowInfo(int totalWindowCount, int locatedWindowCount) {
		this.totalWindowCount = totalWindowCount;
		this.locatedWindowCount = locatedWindowCount;
	}

	@Override
	public String toString() {
		return """
			VideoFaceScannerReport:
			---
			Duration: %s [sec]
			Windows: %s of %s found
			Faces: %s
			---
				""".formatted(duration.toSeconds(), locatedWindowCount, totalWindowCount, faces.size());
	}

}
