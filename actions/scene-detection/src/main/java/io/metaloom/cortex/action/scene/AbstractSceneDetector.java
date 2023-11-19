package io.metaloom.cortex.action.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.VideoFrame;
import io.metaloom.video4j.utils.SimpleImageViewer;

public abstract class AbstractSceneDetector implements SceneDetector {

	private final int videoChopRate = 1;
	private final int videoScaleSize = 512;
	private SimpleImageViewer viewer = new SimpleImageViewer();

	protected void detect(VideoFile video, Detector detector) {
		int showCut = 0;
		double shownDelta = 0;

		for (int nFrame = 41_800; nFrame < video.length(); nFrame += videoChopRate) {
			// System.out.println("Frame: " + nFrame);
			video.seekToFrame(nFrame);
			VideoFrame frame = video.frame();

			BufferedImage img = frame.toImage();
			DetectionResult result = detector.test(img, frame);
			if (result.split()) {
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
			if (result.delta() > result.threshold() / 2) {
				shownDelta = result.delta();
			}
			g.drawString(nFrame + " " + String.format("%.2f", result.delta()) + " D: " + String.format("%.0f", shownDelta), 150,
				img.getHeight() - 20);
			viewer.show(img);

		}
	}
}
