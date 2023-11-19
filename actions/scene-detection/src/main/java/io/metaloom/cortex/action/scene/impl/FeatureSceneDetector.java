package io.metaloom.cortex.action.scene.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.DoubleStream;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import io.metaloom.cortex.action.scene.AbstractSceneDetector;
import io.metaloom.cortex.action.scene.DetectionResult;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.impl.MatProvider;
import io.metaloom.video4j.opencv.CVUtils;
import io.metaloom.video4j.utils.ImageUtils;

public class FeatureSceneDetector extends AbstractSceneDetector {

	private final double FEATURE_THRESHOLD = 1200;
	private final int MAX_CORNORS = 10;

	public FeatureSceneDetector() {
	}

	@Override
	public void detect(VideoFile video) {

		AtomicReference<DetectionVector> prevVector = new AtomicReference<>();
		detect(video, (img, frame) -> {

			MatOfPoint corners = new MatOfPoint();
			double qualityLevel = 0.10f;
			double minDistance = 10f;
			Mat mask = MatProvider.mat();
			int blockSize = 5;
			int gradientSize = 3;
			boolean useHarrisDetector = true;
			double k = 0.04f;
			// Mat greyImage = CVUtils.toGreyScale(frame.mat());
			Mat colorFrame = frame.mat();
			Mat cannyFrame = CVUtils.canny(colorFrame, 60f, 60f);
			Mat greyFrame = MatProvider.mat();
			Mat colorCannyFrame = CVUtils.toBGR(cannyFrame);

			Core.addWeighted(colorCannyFrame, 0.8f, colorFrame, 1f, 1f, greyFrame);

			Graphics g = img.getGraphics();
			g.drawImage(ImageUtils.matToBufferedImage(greyFrame), 1, 1, null);
			greyFrame = CVUtils.toGreyScale(greyFrame);
			Imgproc.goodFeaturesToTrack(greyFrame, corners, MAX_CORNORS, qualityLevel, minDistance, mask, blockSize, gradientSize, useHarrisDetector,
				k);
			// System.out.println("Got: " + corners.toList().size());

			// Show Image
			for (Point p : corners.toList()) {
				g.setColor(Color.RED);
				g.fillOval((int) p.x, (int) p.y, 5, 5);
			}

			EuclideanDistance d = new EuclideanDistance();
			double[] frameAVector = toVector(corners, MAX_CORNORS*2);
			if (prevVector.get() == null) {
				prevVector.set(new DetectionVector(frameAVector));
			}
			printVector(prevVector.get().vector());

			double delta = d.compute(frameAVector, prevVector.get().vector);
			System.out.println(video.currentFrame() + " Delta: " + String.format("%.2f", delta));
			prevVector.set(new DetectionVector(frameAVector));
			return new DetectionResult(delta > FEATURE_THRESHOLD, delta, FEATURE_THRESHOLD);
		});

	}

	record DetectionVector(double[] vector) {

	}

	private double[] toVector(MatOfPoint corners, int dim) {
		DoubleStream stream = corners.toList().stream().sorted((p1,p2) -> {
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

	// CVUtils.harris(frame);
	// CVUtils.toGreyScale(frame);
	// CVUtils.normalize(frame.mat(), frame.mat(), 0, 30);
	// CVUtils.canny(frame, 30f, 30f);

	// Mat hMat = CVUtils.houghLines(frame.mat(), 1, Math.PI / 180, 60, 0f, 0f);
	// Mat hMat2 = CVUtils.houghLinesP(frame.mat(), 1, Math.PI / 180, 50, 50f, 0f);
	// frame.setMat(hMat);
	// CVUtils.houghLines(frame, 1, Math.PI / 180, 50, 50f, 10f);
	// frame.setMat(CVUtils.blur(frame.mat(), new Dimension(50,50), new Point(-1,-1)));
	// CVUtils.toGreyScale(frame);
	// Imgproc.resize(frame.mat(), frame.mat(), new Size(256, 256), 0, 0, Imgproc.INTER_LINEAR);
	// double delta = diff(previousFrame, frame);
	// double delta =0f;

	// SparsePyrLKOpticalFlow flow = SparsePyrLKOpticalFlow.create();
	// if (previousFrame != null) {
	// Mat prevPts = MatProvider.mat();
	// Mat nextPts = MatProvider.mat();
	// Mat status = null;
	// Mat err = null;
	// flow.calc(previousFrame.mat(), frame.mat(), prevPts, nextPts, status, err);
	// }

}
