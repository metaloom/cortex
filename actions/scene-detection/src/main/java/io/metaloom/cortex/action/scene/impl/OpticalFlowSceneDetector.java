package io.metaloom.cortex.action.scene.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.SparsePyrLKOpticalFlow;

import io.metaloom.cortex.action.scene.AbstractSceneDetector;
import io.metaloom.cortex.action.scene.detector.DetectionResult;
import io.metaloom.cortex.media.scene.SceneDetectionResult;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.impl.MatProvider;
import io.metaloom.video4j.opencv.CVUtils;
import io.metaloom.video4j.utils.ImageUtils;

public class OpticalFlowSceneDetector extends AbstractSceneDetector {

	private final int MAX_CORNORS = 600;
	private final double OPTICAL_FLOW_DELTA_THRESHOLD = 0.5f; 
	private final double qualityLevel = 0.05f;
	private final double minDistance = 10f;
	private final int blockSize = 15;
	private final int gradientSize = 3;
	private final boolean useHarrisDetector = false;
	private final double k = 0.04f;
	private final Mat mask = MatProvider.mat();

	@Override
	public SceneDetectionResult detect(VideoFile video) {
		AtomicReference<Mat> previousGray = new AtomicReference<>();
		// AtomicReference<Mat> p0 = new AtomicReference<>(MatProvider.mat());
		// Mat p1 = MatProvider.mat();
		// Mat status = MatProvider.mat();
		Mat err = MatProvider.mat();
		return detect(video, (img, frame) -> {
			Graphics g = img.getGraphics();
			SparsePyrLKOpticalFlow flow = SparsePyrLKOpticalFlow.create();
			Mat colorFrame = frame.mat();
			Mat grayFrame = CVUtils.toGrayScale(colorFrame);
			if (previousGray.get() == null) {
				previousGray.set(grayFrame);
			}

			// Add canny
			Mat cannyFrame = MatProvider.mat();
			Imgproc.Canny(grayFrame, cannyFrame, 50f, 70f);
			Imgproc.dilate(cannyFrame, cannyFrame, MatProvider.mat());

			// Merge gray with canny
			// Mat greyFrame = MatProvider.mat();
			// Mat colorCannyFrame = CVUtils.toBGR(cannyFrame);
			Core.addWeighted(cannyFrame, 0.50f, grayFrame, 1.f, 1.0f, grayFrame);

			// Show enhanced image
			g.drawImage(ImageUtils.matToBufferedImage(grayFrame), 1, 1, null);

			MatOfPoint corners = new MatOfPoint();
			Imgproc.goodFeaturesToTrack(grayFrame, corners, MAX_CORNORS, qualityLevel, minDistance, mask, blockSize, gradientSize,
				useHarrisDetector,
				k);

			MatOfPoint2f p0 = new MatOfPoint2f(corners.toArray());
			MatOfPoint2f p1 = new MatOfPoint2f();
			MatOfPoint2f status = new MatOfPoint2f();
			flow.calc(previousGray.get(), grayFrame, p0, p1, status, err);

			// select good points
			List<Point> goodNew = new ArrayList<>();
			int misses = 0;
			for (int i = 0; i < status.rows(); i++) {
				double[] res = status.get(i, 0);
				// Check if the point was a good tracking
				if (res[0] == 1) {
					// System.out.println(i + " = " + res[0]);
					double[] po = p1.get(i, 0);
					// System.out.println(po[0] + " " + po[1]);
					goodNew.add(new Point(po[0], po[1]));
				} else {
					misses++;
				}
			}

			float ratio = (float) misses / (float) status.rows();
			// System.out.println("R: " +String.format("%.2f",ratio)+ " Misses: " + misses + " " + status.rows());

			for (Point p : goodNew) {
				g.setColor(Color.RED);
				g.drawOval((int) p.x, (int) p.y, 4, 4);
			}
			previousGray.set(grayFrame);

			return new DetectionResult(ratio, OPTICAL_FLOW_DELTA_THRESHOLD);
		});

	}

}
