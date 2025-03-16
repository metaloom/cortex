package io.metaloom.loom.cortex.action.facedetect;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.facedetect.video.VideoFaceScanner;
import io.metaloom.cortex.action.facedetect.video.VideoFaceScannerReport;
import io.metaloom.utils.FloatUtils;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.Videos;
import io.metaloom.video4j.utils.ImageUtils;
import io.metaloom.video4j.utils.SimpleImageViewer;

public class VideoFaceScannerTest {

	private SimpleImageViewer viewer = new SimpleImageViewer();

	VideoFaceScanner detector = new VideoFaceScanner();
	// private static final int WINDOW_COUNT = 30;
	// private static final int WINDOW_SIZE = 120;
	// private static final int WINDOW_STEPS = 5;

	private static final int WINDOW_COUNT = 50;

	@Test
	public void testExampleCode() throws InterruptedException, IOException, URISyntaxException {
		try (VideoFile video = Videos.open("/extra/vid/1.avi")) {
			System.out.println(video.height() + " x " + video.width());
			long start = System.currentTimeMillis();
			VideoFaceScannerReport report = detector.scan(video, WINDOW_COUNT);
			long dur = System.currentTimeMillis() - start;
			System.out.println("Scan took " + dur + " ms / " + (dur / 1000) + " s");
			for (Face face : report.getFaces()) {
				double b = face.get("blurriness");
				System.out.println("Face [" + b + "][" + face.get("frame") + "] " + Arrays.toString(face.getEmbedding()));
				BufferedImage img = (BufferedImage) face.get("image");
				Graphics g = img.getGraphics();
				g.setColor(Color.RED);
				g.drawString("B: " + b, 10, 10);
				g.dispose();
				ImageUtils.show(img);
			}

			// for (List<Face> cluster : clusterFaces(report.getFaces())) {
			// for (Face face : cluster) {
			// ImageUtils.show((BufferedImage) face.get("image"));
			// }
			// System.in.read();
			// System.out.println("NEXT");
			// }
		}
		System.out.println("Done");
		System.in.read();

	}

	private List<List<Face>> clusterFaces(List<Face> faces) {
		System.out.println("Faces for cluster: " + faces.size());
		DBSCANClusterer<DoublePoint> clusterAlgo = new DBSCANClusterer<>(30.0, 2);
		List<DoublePoint> ar = new ArrayList<>();
		faces.stream().forEach(facedescriptor -> {
			ar.add(new DoublePoint(FloatUtils.toDouble(facedescriptor.getEmbedding())));
		});

		List<Cluster<DoublePoint>> clusters = clusterAlgo.cluster(ar);
		List<List<Face>> clusterlist = new ArrayList<>();
		for (int i = 0; i < clusters.size(); i++) {
			Cluster<DoublePoint> cluster = clusters.get(i);
			String label = "P: " + i;
			List<Face> clusterfaces = new ArrayList<>();
			cluster.getPoints().stream().forEach(point -> {
				faces.stream().filter(
					facedescriptor -> Arrays.equals(FloatUtils.toDouble(facedescriptor.getEmbedding()), point.getPoint()))
					.forEach(facedescriptor -> {
						facedescriptor.setLabel(label);
						clusterfaces.add(facedescriptor);
					});
			});
			clusterlist.add(clusterfaces);
		}
		System.out.println("Cluster: " + clusterlist.size());
		return clusterlist;
	}

	// @Test
	// public void testWindowScan() throws InterruptedException {
	// try (VideoFile video = Videos.open("/extra/vid/4.mkv")) {
	// List<FrameWindow> windows = detector.locateWindows(video, 2);
	// detector.fineTuneWindow(video, windows.get(0));
	// }
	//
	// }
}
