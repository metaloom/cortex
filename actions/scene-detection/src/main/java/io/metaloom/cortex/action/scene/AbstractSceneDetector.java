package io.metaloom.cortex.action.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import io.metaloom.cortex.action.scene.detector.DetectionResult;
import io.metaloom.cortex.action.scene.detector.Detector;
import io.metaloom.cortex.action.scene.detector.SceneDetectionResult;
import io.metaloom.cortex.action.scene.detector.SceneDetector;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.VideoFrame;
import io.metaloom.video4j.utils.SimpleImageViewer;

public abstract class AbstractSceneDetector implements SceneDetector {

	private final int videoChopRate = 1;
	protected final int VIDEO_SCALE_SIZE = 512;
	private SimpleImageViewer viewer = new SimpleImageViewer();

	protected SceneDetectionResult detect(VideoFile video, Detector detector) {
		int showCut = 0;
		double shownDelta = 0;
		SceneDetectionResult result = new SceneDetectionResult(); 
		for (int nFrame = 21_800; nFrame < video.length(); nFrame += videoChopRate) {
			// System.out.println("Frame: " + nFrame);
			video.seekToFrame(nFrame);
			VideoFrame frame = video.frame();

			Imgproc.resize(frame.mat(), frame.mat(), new Size(VIDEO_SCALE_SIZE, VIDEO_SCALE_SIZE), 0, 0, Imgproc.INTER_LINEAR);

			BufferedImage img = frame.toImage();
			DetectionResult frameResult = detector.test(img, frame);
			boolean isSplit = frameResult.delta() > frameResult.threshold();
			if (isSplit) {
				showCut = 1;
			}

			// int diff = (int) ((delta / 100) * 3);
			// System.out.println(nFrame + " " + diff + " " + String.format("%.2f", delta));
			// Color color = new Color(red, 0, 0);
			// g.setColor(color);
			// g.fillRect(10, 10, 10, 10);
			Graphics g = img.getGraphics();
			if (showCut != 0 && showCut < 8) {
				g.setColor(Color.GREEN);
				g.fillRect(10, 10, 50, 50);
				showCut++;
			} else {
				showCut = 0;
			}
			g.setColor(Color.WHITE);
			g.setFont(new Font("TimesRoman", Font.BOLD, 20));
			if (frameResult.delta() > frameResult.threshold() / 2) {
				shownDelta = frameResult.delta();
			}
			g.drawString(nFrame + " " + String.format("%.2f", frameResult.delta()) + " D: " + String.format("%.0f", shownDelta), 30,
				img.getHeight() - 20);
			viewer.show(img);

		}
		return result;
	}
}
