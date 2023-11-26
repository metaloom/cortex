package io.metaloom.cortex.action.facedetect.video;

import io.metaloom.video.facedetect.FaceVideoFrame;

public record FrameWindow(int nWindow, FaceVideoFrame frame, int height, int width) {

}
