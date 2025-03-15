package io.metaloom.loom.cortex.action.facedetect;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.facedetect.video.VideoFaceScannerReport;

public class VideoFaceScannerReportTest {

	@Test
	public void testReport() {
		VideoFaceScannerReport report = new VideoFaceScannerReport();
		report.setFaces(Collections.emptyList());
		report.setWindowInfo(10, 8);
		System.out.println(report);
	}
}
