package io.metaloom.cortex.action.facedetect.video;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.facedetection.client.FaceDetectionServerClient;
import io.metaloom.facedetection.client.model.DetectionResponse;
import io.metaloom.facedetection.client.model.FaceModel;
import io.metaloom.video.facedetect.FaceVideoFrame;
import io.metaloom.video.facedetect.FacedetectorUtils;
import io.metaloom.video.facedetect.dlib.impl.DLibFacedetector;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video.facedetect.face.FaceBox;
import io.metaloom.video4j.Video4j;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.VideoFrame;
import io.metaloom.video4j.opencv.CVUtils;
import io.metaloom.video4j.utils.ImageUtils;

public class VideoFaceScanner {

	private final static Logger logger = LoggerFactory.getLogger(VideoFaceScanner.class);

	public static final String FACE_DETECT_SERVER_BASEURL = "http://localhost:8001/api/v1";

	public static final String BLURRINESS_KEY = "blurriness";

	public static final String IMAGE_KEY = "image";

	private static DLibFacedetector DLIB_DETECTOR;
	private static FaceDetectionServerClient client;
	// private SimpleImageViewer viewer = new SimpleImageViewer();
	// private SimpleImageViewer viewer2 = new SimpleImageViewer();

	static {
		Video4j.init();
		try {
			DLIB_DETECTOR = DLibFacedetector.create();
			DLIB_DETECTOR.setMinFaceHeightFactor(0.01f);
			DLIB_DETECTOR.enableCNNDetector();
			client = FaceDetectionServerClient.newBuilder()
				.setBaseURL(FACE_DETECT_SERVER_BASEURL).build();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public VideoFaceScannerReport scan(VideoFile video, int windowCount, int windowSize, int windowSteps) throws InterruptedException {
		VideoFaceScannerReport report = new VideoFaceScannerReport();

		// Locate potential windows
		List<FrameWindow> windows = identifyPotentialWindows(video, windowCount);
		report.setWindowInfo(windowCount, windows.size());
		logger.info("Window scan result: {} windows with faces.", windows.size());

		// Process the windows and locate the best faces
		List<Face> allFaces = new ArrayList<>();
		for (FrameWindow window : windows) {
			allFaces.addAll(fineTuneWindow(video, window, windowSize, windowSteps));
		}

		// Now process all found faces
		for (Face dlibFace : allFaces) {
			try {
				processFace(dlibFace);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		report.setFaces(allFaces.stream().filter(Face::hasEmbedding).toList());
		logger.info("Got total of " + allFaces.size() + " faces with embeddings in " + windows.size() + " windows");
		return report;
	}

	/**
	 * Scan the given window of the video and locate the best faces.
	 * 
	 * @param video
	 * @param window
	 * @param windowSize
	 * @param windowSteps
	 * @return
	 * @throws InterruptedException
	 */
	public List<Face> fineTuneWindow(VideoFile video, FrameWindow window, int windowSize, int windowSteps) throws InterruptedException {
		long nWindowFrame = window.frame().number();
		if (logger.isDebugEnabled()) {
			logger.debug("Tuning window: " + window.nWindow());
		}

		List<Face> faces = new ArrayList<>();
		// Backward
		long nFrameStart = nWindowFrame - windowSize;
		faces.addAll(scanWindow(video, window.nWindow(), nFrameStart, nWindowFrame, windowSteps));
		// Forward
		long nFrameEnd = nWindowFrame + windowSize;
		faces.addAll(scanWindow(video, window.nWindow(), nWindowFrame, nFrameEnd, windowSteps));

		faces = faces.stream().sorted((f1, f2) -> {
			double b1 = f1.get(BLURRINESS_KEY);
			double b2 = f2.get(BLURRINESS_KEY);
			return Double.compare(b2, b1);
		}).limit(1).toList();

		return faces;
	}

	/**
	 * Scan the given window area for faces using dlib. Run a bluriness filter and return only the top two faces.
	 * 
	 * @param video
	 * @param nWindow
	 * @param from
	 * @param to
	 * @param windowSteps
	 * @return
	 */
	private List<Face> scanWindow(VideoFile video, int nWindow, long from, long to, int windowSteps) {
		int nNoFaceFrame = 0;
		List<Face> faces = new ArrayList<>();
		for (long nFrame = from; nFrame < to; nFrame += windowSteps) {
			FaceVideoFrame frame = dlibDetectFaces(video, nFrame);
			if (frame != null && frame.hasFaces()) {
				// DLIB_DETECTOR.markFaces(frame);
				// DLIB_DETECTOR.markLandmarks(frame);
				// viewer2.show(frame);

				// Process each found dlib face
				for (Face face : frame.faces()) {
					face.set("frame", frame.number());
					// Crop to the face and calculate the blurriness
					System.out.println("Crop to: " + face + " from " + frame.height() + " x " + frame.width());

					Mat faceImage = frame.mat().clone();
					int padding = (int) ((double) face.box().getWidth() * 1.5d);
					FacedetectorUtils.cropToFace(faceImage, face, padding);
					// ImageUtils.show(faceImage);
					double blurriness = CVUtils.blurriness(faceImage);
					face.set(BLURRINESS_KEY, blurriness);
					BufferedImage croppedFaceImage = ImageUtils.matToBufferedImage(faceImage);
					// croppedFaceImage = ImageUtils.scale(croppedFaceImage, croppedFaceImage.getWidth()*2, croppedFaceImage.getHeight()*2);
					// BufferedImage croppedFaceImage = ImageUtils.matToBufferedImage(frame.mat());
					face.set(IMAGE_KEY, croppedFaceImage);
					faceImage.release();
					logger.info("Adding face for window " + nWindow + " now " + faces.size() + " in total for this window.");
					faces.add(face);
				}
				nNoFaceFrame = 0;
			} else {
				nNoFaceFrame++;
			}
			if (nNoFaceFrame > 5) {
				logger.info("No more faces found in window " + nWindow);
				break;
			}

		}

		// Sort by blurriness and return the found two

		// .map(face -> {
		// double f = face.get(BLURRINESS_KEY);
		// BufferedImage img = face.get(IMAGE_KEY);
		// Graphics g = img.getGraphics();
		// g.setColor(Color.RED);
		// g.drawString("B: " + f, 10, 10);
		// g.dispose();
		// ImageUtils.show(img);
		// return face;
		// }).toList();

		// try {
		// System.in.read();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		logger.info("Faces: " + faces.size());
		return faces;
	}

	private FaceVideoFrame dlibDetectFaces(VideoFile video, long nFrame) {
		video.seekToFrame(nFrame);
		VideoFrame frame = video.frame();
		Mat original = frame.mat();

		// Resize to 512
		Mat smaller = original.clone();
		double aspectRatio = (double) original.height() / (double) original.width();
		int width = (int) (512d * aspectRatio);
		CVUtils.resize(smaller, smaller, 512, width);
		frame.setMat(smaller);

		// Run detection
		FaceVideoFrame videoFrame = DLIB_DETECTOR.detectFaces(frame);

		// Reset original and free smaller version
		videoFrame.setMat(original);
		frame.setMat(original);

		for (Face face : videoFrame.faces()) {
			FaceBox box = face.box();

			double startXFactor = (double) box.getStartX() / (double) smaller.width();
			box.setStartX((int) ((double) original.width() * startXFactor));

			double startYFactor = (double) box.getStartY() / (double) smaller.height();
			box.setStartY((int) ((double) original.height() * startYFactor));

			double heightFactor = (double) box.getHeight() / (double) smaller.height();
			box.setHeight((int) ((double) original.height() * heightFactor));

			double widthFactor = (double) box.getWidth() / (double) smaller.width();
			box.setWidth((int) ((double) original.width() * widthFactor));

			// VideoFrame image = FacedetectorUtils.cropToFace(videoFrame, face);
			// ImageUtils.show(image.mat());
		}
		smaller.release();
		return videoFrame;
	}

	private void processFace(Face dlibFace) {
		BufferedImage croppedFaceImage = dlibFace.get("image");
		try {
			// Graphics g = croppedFaceImage.getGraphics();
			// g.setColor(Color.RED);
			// g.drawString("B: " + blurriness, 10, 10);
			// g.dispose();
			// 2. Send image to
			// ImageUtils.show(croppedFaceImage);
			String imageData = ImageUtils.toBase64JPG(croppedFaceImage);
			DetectionResponse response = client.detectByImageData(imageData);
			List<FaceModel> detectedFaces = response.getFaces();
			System.out.println("FACES: " + detectedFaces.size());
			if (detectedFaces.size() > 1) {
				logger.warn("I found more than one face in the cropped face image.");
				return;
			}
			// ArrayList<? extends Face> faces = new ArrayList<>();
			// ImageUtils.show(croppedFaceImage);
			for (FaceModel face : detectedFaces) {
				dlibFace.setEmbedding(face.getEmbedding());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Scan the video and segment it in the given amount of windows. A window is an area in the video which will be inspected for faces.
	 * 
	 * @param video
	 * @param windowCount
	 * @return
	 * @throws InterruptedException
	 */
	public List<FrameWindow> identifyPotentialWindows(VideoFile video, int windowCount) throws InterruptedException {
		List<FrameWindow> windows = new ArrayList<>();
		long totalFrames = video.length();
		long spread = totalFrames / windowCount;
		logger.info("Window scan using spread: " + spread + " potential window count: " + (totalFrames / spread));

		int nWindow = 0;
		for (long nFrame = spread; nFrame < totalFrames - spread; nFrame += spread) {
			logger.info("Scanning frame for potential window " + nFrame);
			video.seekToFrame(nFrame);
			VideoFrame frame = video.frame();
			CVUtils.resize(frame, 512);
			FaceVideoFrame faceFrame = DLIB_DETECTOR.detectFaces(frame);

			// The middle frame of the vindow contains a face - thus add it to the list of windows.
			if (faceFrame.hasFaces()) {
				logger.info("[W" + nWindow + "] Adding window with " + faceFrame.faces().size() + " faces.");
				// DLIB_DETECTOR.markLandmarks(faceFrame);
				// DLIB_DETECTOR.markFaces(faceFrame);
				windows.add(new FrameWindow(nWindow, faceFrame, faceFrame.height(), faceFrame.width()));
				// viewer.show(faceFrame);
				nWindow++;
			}
		}

		return windows;
	}

}
