package io.metaloom.cortex.action.scene.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

import io.metaloom.cortex.action.scene.AbstractSceneDetector;
import io.metaloom.cortex.action.scene.detector.DetectionResult;
import io.metaloom.cortex.media.scene.SceneDetectionResult;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.VideoFrame;
import io.metaloom.video4j.impl.MatProvider;
import io.metaloom.video4j.opencv.CVUtils;

public class HistogramSceneDetector extends AbstractSceneDetector {

	@Override
	public SceneDetectionResult detect(VideoFile video) {

		final double HIST_THRESHOLD = 0.2f;
		AtomicReference<VideoFrame> prevFrame = new AtomicReference<>();
		return detect(video, (img, frame) -> {
			Mat cannyFrame = CVUtils.canny(frame.mat(), 30f, 30f);
			cannyFrame = CVUtils.toBGR(cannyFrame);
			Mat colorFrame = frame.mat();
			Mat dst = MatProvider.mat();
			Core.addWeighted(cannyFrame, 0.6f, colorFrame, 0.2f, 1f, dst);
			frame.setMat(dst);
			double delta = diff(frame, prevFrame.get());
			prevFrame.set(frame);
			return new DetectionResult(delta, HIST_THRESHOLD);
		});

	}

	private double diff(VideoFrame previousFrame, VideoFrame frame) {
		// Mat hsvFrameA = MatProvider.mat();
		// Mat hsvFrameB = MatProvider.mat();
		// Imgproc.cvtColor(previousFrame.mat(), hsvFrameA, Imgproc.COLOR_BGR2HSV);
		// Imgproc.cvtColor(frame.mat(), hsvFrameB, Imgproc.COLOR_BGR2HSV);

		int hBins = 50, sBins = 60;
		int[] histSize = { hBins, sBins };

		float[] ranges = { 0, 256, 0, 256 };
		// int channels = 3;
		int[] channels = { 0, 1 };

		// Imgproc.calcHist(Arrays.asList(frame.mat()), 3, new MatOfInt(3), new Mat(), null, new MatOfInt(buckets), new MatOfFloat(ranges), false);
		// double baseBase = Imgproc.compareHist( histBase, histBase, compareMethod );
		Mat histFrameA = calcHistogram(previousFrame.mat(), channels, histSize, ranges);
		Mat histFrameB = calcHistogram(frame.mat(), channels, histSize, ranges);

		double histCompareResult = Imgproc.compareHist(histFrameA, histFrameB, 1);
		// System.out.println("hist: " + String.format("%.2f", histCompareResult));
		return histCompareResult;

	}

	private Mat calcHistogram(Mat mat, int[] channels, int[] histSize, float[] ranges) {
		Mat hist = MatProvider.mat();
		List<Mat> hsvBaseList = Arrays.asList(mat);
		Imgproc.calcHist(hsvBaseList, new MatOfInt(channels), new Mat(), hist, new MatOfInt(histSize), new MatOfFloat(ranges), false);
		Core.normalize(hist, hist, 0, 1, Core.NORM_MINMAX);
		return hist;
	}

}
