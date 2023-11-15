package io.metaloom.loom.cortex.action.facedetect.video;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.video.facedetect.Face;
import io.metaloom.video.facedetect.FaceVideoFrame;
import io.metaloom.video.facedetect.FacedetectorUtils;
import io.metaloom.video.facedetect.dlib.impl.DLibFacedetector;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.VideoFrame;
import io.metaloom.video4j.opencv.CVUtils;

public class VideoFaceScanner {

	private final static Logger log = LoggerFactory.getLogger(VideoFaceScanner.class);

	private static DLibFacedetector DLIB_DETECTOR;
	// private SimpleImageViewer viewer = new SimpleImageViewer();
	// private SimpleImageViewer viewer2 = new SimpleImageViewer();

	static {
		Video4j.init();
		try {
			DLIB_DETECTOR = DLibFacedetector.create();
			DLIB_DETECTOR.setMinFaceHeightFactor(0.01f);
			DLIB_DETECTOR.enableCNNDetector();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public List<Face> scan(VideoFile video, int windowCount, int windowSize, int windowSteps) throws InterruptedException {
		List<FrameWindow> windows = locateWindows(video, windowCount);
		log.info("Window scan result: " + windows.size() + " windows with faces.");
		List<Face> allFaces = new ArrayList<>();
		System.out.println();
		for (FrameWindow window : windows) {
			allFaces.addAll(fineTuneWindow(video, window, windowSize, windowSteps));
		}

		log.info("Got total of " + allFaces.size() + " faces with embeddings in " + windows.size() + " windows");
		return allFaces;
	}

	public List<Face> fineTuneWindow(VideoFile video, FrameWindow window, int windowSize, int windowSteps) throws InterruptedException {
		long nWindowFrame = window.frame().number();
		long nFrameStart = nWindowFrame - windowSize;
		long nFrameEnd = nWindowFrame + windowSize;
		System.out.println("Tuning window: " + window.nWindow());

		List<Face> faces = new ArrayList<>();
		// Backward
		faces.addAll(scanWindow(video, window.nWindow(), nFrameStart, nWindowFrame, windowSteps));
		// Forward
		faces.addAll(scanWindow(video, window.nWindow(), nWindowFrame, nFrameEnd, windowSteps));
		return faces;
	}

	private Collection<? extends Face> scanWindow(VideoFile video, int nWindow, long from, long to, int windowSteps) {
		int nNoFaceFrame = 0;
		List<Face> faces = new ArrayList<>();
		for (long nFrame = from; nFrame < to; nFrame += windowSteps) {
			FaceVideoFrame frame = scanDetailFrame(video, nFrame);
			if (frame != null && frame.hasFace()) {
				// DLIB_DETECTOR.markFaces(frame);
				// DLIB_DETECTOR.markLandmarks(frame);
				// viewer2.show(frame);
				for (Face face : frame.faces()) {
					if (face.hasEmbedding()) {
						face.set("frame", frame.number());
						VideoFrame faceImage = FacedetectorUtils.cropToFace(frame, face);
						face.set("image", faceImage.toImage());
						log.info("Adding face for window " + nWindow + " now " + faces.size() + " in total for this window.");
						faces.add(face);
					} else {
						log.info("No embedding");
					}
				}
				nNoFaceFrame = 0;
			} else {
				nNoFaceFrame++;
			}
			if (nNoFaceFrame > 5) {
				log.info("No more faces found in window " + nWindow);
				break;
			}

		}
		log.info("Faces: " + faces.size());
		return faces;
	}

	private FaceVideoFrame scanDetailFrame(VideoFile video, long nFrame) {
		video.seekToFrame(nFrame);

		VideoFrame frame = video.frame();
		FaceVideoFrame faceFrame = DLIB_DETECTOR.detectEmbeddings(frame);
		// DLIB_DETECTOR.markLandmarks(faceFrame);
		// DLIB_DETECTOR.markFaces(faceFrame);
		if (faceFrame.hasFace()) {
			return faceFrame;
		} else {
			return null;
		}

	}

	public List<FrameWindow> locateWindows(VideoFile video, int windowCount) throws InterruptedException {
		List<FrameWindow> windows = new ArrayList<>();
		long totalFrames = video.length();
		long spread = totalFrames / windowCount;
		log.info("Window scan using spread: " + spread + " potential window count: " + (totalFrames / spread));

		int nWindow = 0;
		for (long nFrame = spread; nFrame < totalFrames - spread; nFrame += spread) {
			log.info("Scanning frame for potential window " + nFrame);
			video.seekToFrame(nFrame);
			VideoFrame frame = video.frame();
			CVUtils.resize(frame, 512);
			// viewer.show(frame);
			FaceVideoFrame faceFrame = DLIB_DETECTOR.detectFaces(frame);
			if (faceFrame.hasFace()) {
				log.info("[W" + nWindow + "] Adding window with " + faceFrame.faces().size() + " faces.");
				DLIB_DETECTOR.markFaces(faceFrame);
				windows.add(new FrameWindow(nWindow, faceFrame, faceFrame.height(), faceFrame.width()));
				nWindow++;
			}
		}

		return windows;
	}

}
