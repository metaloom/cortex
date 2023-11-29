package io.metaloom.cortex.action.scene.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.DoubleStream;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import io.metaloom.cortex.action.scene.AbstractSceneDetector;
import io.metaloom.cortex.action.scene.detector.DetectionResult;
import io.metaloom.cortex.action.scene.detector.SceneDetectionResult;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.impl.MatProvider;
import io.metaloom.video4j.opencv.CVUtils;
import io.metaloom.video4j.utils.ImageUtils;

public class FeatureSceneDetector extends AbstractSceneDetector {

	private final double FEATURE_THRESHOLD = 2000;
	private final int MAX_CORNORS = 100;

	public FeatureSceneDetector() {
	}

	@Override
	public SceneDetectionResult detect(VideoFile video) {

		AtomicReference<DetectionVector> prevVector = new AtomicReference<>();
		return detect(video, (img, frame) -> {

			MatOfPoint corners = new MatOfPoint();
			double qualityLevel = 0.30f;
			double minDistance = 10f;
			Mat mask = MatProvider.mat();
			int blockSize = 15;
			int gradientSize = 3;
			boolean useHarrisDetector = false;
			double k = 0.04f;
			// Mat greyImage = CVUtils.toGreyScale(frame.mat());
			Mat colorFrame = frame.mat();
			Mat grayFrame = CVUtils.toGrayScale(colorFrame);
			// CVUtils.increaseContrast(grayFrame, grayFrame, 0.05f, 0.5f);

			// Mat cannyFrame = CVUtils.canny(greyFrame, 60f, 60f);
			Mat cannyFrame = MatProvider.mat();
			Imgproc.Canny(grayFrame, cannyFrame, 50f, 70f);
			Imgproc.dilate(cannyFrame, cannyFrame, MatProvider.mat());

			// Mat greyFrame = MatProvider.mat();
			// Mat colorCannyFrame = CVUtils.toBGR(cannyFrame);
			// Core.addWeighted(colorCannyFrame, 0.25f, colorFrame, 1.f, 1.0f, grayFrame);

			Graphics g = img.getGraphics();
			g.drawImage(ImageUtils.matToBufferedImage(cannyFrame), 1, 1, null);
			Imgproc.goodFeaturesToTrack(cannyFrame, corners, MAX_CORNORS, qualityLevel, minDistance, mask, blockSize, gradientSize, useHarrisDetector,
				k);
			// System.out.println("Got: " + corners.toList().size());

			// Show Image
			for (Point p : corners.toList()) {
				g.setColor(Color.RED);
				g.fillOval((int) p.x, (int) p.y, 5, 5);
			}

			EuclideanDistance d = new EuclideanDistance();
			double[] frameAVector = toVector(corners, MAX_CORNORS * 2);
			if (prevVector.get() == null) {
				prevVector.set(new DetectionVector(frameAVector));
			}
			printVector(prevVector.get().vector());

			double delta = d.compute(frameAVector, prevVector.get().vector);
			System.out.println(video.currentFrame() + " Delta: " + String.format("%.2f", delta));
			prevVector.set(new DetectionVector(frameAVector));
			return new DetectionResult( delta, FEATURE_THRESHOLD);
		});

	}

	record DetectionVector(double[] vector) {

	}

	private double[] toVector(MatOfPoint corners, int dim) {
		DoubleStream stream = corners.toList().stream().sorted((p1, p2) -> {
			return Double.compare(p1.x, p2.x);
		}).mapMultiToDouble((p, consumer) -> {
			consumer.accept(p.x);
			consumer.accept(p.y);
		});
		double[] v1 = stream.toArray();
		// double[] vector = new double[maxCorners];
		// System.arraycopy(v1, 0, vector, 0, maxCorners);
		double[] res = Arrays.copyOf(v1, dim);
		System.out.println();
		printVector(res);
		return res;

	}

	private void printVector(double[] res) {
		for (int i = 0; i < res.length; i++) {
			System.out.print(res[i] + " ");
		}
		System.out.println();
	}

}
