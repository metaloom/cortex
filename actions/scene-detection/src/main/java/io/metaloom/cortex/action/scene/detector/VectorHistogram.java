package io.metaloom.cortex.action.scene.detector;

import java.awt.Color;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

import io.metaloom.video4j.VideoFrame;

public class VectorHistogram {

	public static double compareHistogram(VideoFrame previousFrame, VideoFrame frame) {

		if (previousFrame == null) {
			return 0;
		}
		EuclideanDistance d = new EuclideanDistance();

		double[] frameAVector = toVector(previousFrame);
		double[] frameBVector = toVector(frame);
		return d.compute(frameAVector, frameBVector);
	}

	private static double[] toVector(VideoFrame frame) {
		int buckets = 16;

		double[] vector = new double[buckets];
		// int[] counts = new int[buckets];
		for (int x = 1; x < frame.width(); x++) {
			for (int y = 1; y < frame.height(); y++) {
				double[] pixel = frame.mat().get(y, x);
				// int rbgPixel = new Color((float)pixel[0]/255, (float)pixel[1]/255, (float)pixel[2]/255).getRGB();

				int rgbPixel = 0;
				if (pixel.length == 1) {
					rgbPixel = (int) pixel[0];
					// rbgPixel = new Color(greyLevel, greyLevel, greyLevel).getRGB();
				} else {
					rgbPixel = new Color((int) pixel[0], (int) pixel[1], (int) pixel[2]).getRGB();
				}
				int v = (rgbPixel & 0xff) * buckets / 256;
				// vector[x + y + 1] = pixel[0];
				vector[v]++;
			}
		}

		return vector;
	}
}
