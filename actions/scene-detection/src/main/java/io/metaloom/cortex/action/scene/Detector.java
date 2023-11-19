package io.metaloom.cortex.action.scene;

import java.awt.image.BufferedImage;

import io.metaloom.video4j.VideoFrame;

@FunctionalInterface
public interface Detector {

	DetectionResult test(BufferedImage previewImage, VideoFrame frame);
}
