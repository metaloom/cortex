package io.metaloom.cortex.action.facedetect.video;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
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
import io.metaloom.video.facedetect.insightface.impl.InsightfaceFacedetector;
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

	/**
	 * Size to which the frame should be scaled down to before running initial face detection
	 */
	public static final int DETECTION_SCALE_SIZE = 512;

	private static final double BLUR_THRESHOLD = 3;

	private static DLibFacedetector DLIB_DETECTOR;
	private static InsightfaceFacedetector INSIGHTFACE_DETECTOR;
	private static FaceDetectionServerClient client;
	// private SimpleImageViewer viewer = new SimpleImageViewer();
	// private SimpleImageViewer viewer2 = new SimpleImageViewer();

	static {
		Video4j.init();
		try {
			DLIB_DETECTOR = DLibFacedetector.create();
			DLIB_DETECTOR.setMinFaceHeightFactor(0.01f);
			DLIB_DETECTOR.enableCNNDetector();
			INSIGHTFACE_DETECTOR = InsightfaceFacedetector.create();
			client = FaceDetectionServerClient.newBuilder()
				.setBaseURL(FACE_DETECT_SERVER_BASEURL).build();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public VideoFaceScannerReport scan(VideoFile video, int windowCount)
		throws InterruptedException, IOException, URISyntaxException {
		VideoFaceScannerReport report = new VideoFaceScannerReport();

		// Locate potential windows
		// List<FrameWindow> windows = identifyPotentialWindows(video, windowCount);
		List<FrameWindow> windows = splitWindows(video, windowCount);
		report.setWindowInfo(windowCount, windows.size());
		// logger.info("Window scan result: {} windows with faces.", windows.size());

		// Process the windows and locate the best faces
		List<Face> allFaces = new ArrayList<>();
		for (FrameWindow window : windows) {
			allFaces.addAll(processWindow(video, window));
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
		logger.info("Got total of {} faces with embeddings in {} windows", allFaces.size(), windows.size());
		return report;
	}

	/**
	 * @throws URISyntaxException
	 * @throws IOException
	 *             Scan the given window of the video and locate the best faces.
	 * 
	 * @param video
	 * @param window
	 * @param windowSize
	 * @param windowSteps
	 * @return
	 * @throws InterruptedException
	 */
	public List<Face> processWindow(VideoFile video, FrameWindow window)
		throws InterruptedException, IOException, URISyntaxException {
		if (logger.isDebugEnabled()) {
			logger.debug("Tuning window: {}", window);
		}

		List<Face> faces = scanWindow(video, window, 15);
		faces = faces.stream().sorted((f1, f2) -> {
			double b1 = f1.get(BLURRINESS_KEY);
			double b2 = f2.get(BLURRINESS_KEY);
			return Double.compare(b2, b1);
		}).limit(1).toList();

		return faces;
	}

	/**
	 * @throws InterruptedException
	 * @throws URISyntaxException
	 * @throws IOException
	 *             Scan the given window area for faces using dlib. Run a blurriness filter and return only the top two faces.
	 * 
	 * @param video
	 * @param nWindow
	 * @param from
	 * @param to
	 * @param windowSteps
	 * @return
	 */
	private List<Face> scanWindow(VideoFile video, FrameWindow window, int windowSteps)
		throws IOException, URISyntaxException, InterruptedException {

		int nWindow = window.number();
		long from = window.from();
		long to = window.to();

		List<Face> faces = new ArrayList<>();
		long start = System.currentTimeMillis();
		long nFrame = from;
		System.out.println("Scanning window " + from + " to " + to + " with steps " + windowSteps);
		while (nFrame + windowSteps < to) {

			// Skip frames
			// long startSkip = System.currentTimeMillis();
			nFrame += windowSteps;
			video.seekToFrame(nFrame);
			VideoFrame frame = video.frame();
			// for (int i = 0; i < windowSteps; i++) {
			// frame = video.frame();
			// }
			// System.out.println("Skip took: " + (System.currentTimeMillis() - startSkip));

			// Stop processing when we found 10 faces
			if (faces.size() > 3) {
				System.out.println("Got enough faces");
				break;
			}

			List<Face> dlibFaces = dlibProcessFrame(frame);
			faces.addAll(dlibFaces);

			// No faces found. Lets skip a few frames
			if (dlibFaces.isEmpty()) {
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
		logger.info("Faces: {}, window: {}", faces.size(), nWindow);
		return faces;
	}

	private List<Face> dlibProcessFrame(VideoFrame frame) {
		List<Face> faces = new ArrayList<>();
		FaceVideoFrame faceFrame = dlibDetectFaces(frame);
		// FaceVideoFrame faceFrame = insightfaceDetectFaces(frame);
		
		if (faceFrame != null && faceFrame.hasFaces()) {
			// DLIB_DETECTOR.markFaces(frame);
			// DLIB_DETECTOR.markLandmarks(frame);
			// viewer2.show(frame);

			// Process each found dlib face
			for (Face face : faceFrame.faces()) {
				face.set("frame", faceFrame.number());
				// Crop to the face and calculate the blurriness
				// System.out.println("Crop to: " + face + " from " + faceFrame.height() + " x " + faceFrame.width());

				// Mat faceImage = faceFrame.mat().clone();
				int padding = (int) ((double) face.box().getWidth() * 1d);
				Mat faceImage = FacedetectorUtils.cropToFace(faceFrame.mat(), face, padding);
				// ImageUtils.show(faceImage);
				double blurriness = CVUtils.blurriness(faceImage);
//				if (blurriness < BLUR_THRESHOLD) {
//					logger.warn("Omitting face due to blur check: {}", blurriness);
					// Skipped due to bad quality
					// continue;
//				}
				face.set(BLURRINESS_KEY, blurriness);
				BufferedImage croppedFaceImage = ImageUtils.matToBufferedImage(faceImage);
				// System.out.println("Cropped: " + croppedFaceImage.getWidth() + " x " + croppedFaceImage.getHeight());
				// croppedFaceImage = ImageUtils.scale(croppedFaceImage, croppedFaceImage.getWidth()*2, croppedFaceImage.getHeight()*2);
				// BufferedImage croppedFaceImage = ImageUtils.matToBufferedImage(frame.mat());
				face.set(IMAGE_KEY, croppedFaceImage);
				// faceImage.release();
				// logger.info("Adding face for window " + nWindow + " now " + faces.size() + " in total for this window.");
				faces.add(face);
			}

		}
		return faces;

	}

	private FaceVideoFrame dlibDetectFaces(VideoFrame frame) {
		long start = System.currentTimeMillis();
		Mat original = frame.mat();

		boolean scaleDown = frame.height() >= DETECTION_SCALE_SIZE + 128;
		Mat smaller = null;
		if (scaleDown) {
			// Resize to smaller size for detection
			smaller = original.clone();
			double aspectRatio = (double) original.height() / (double) original.width();
			int width = (int) ((double) DETECTION_SCALE_SIZE * aspectRatio);
			CVUtils.resize(smaller, smaller, DETECTION_SCALE_SIZE, width);
			frame.setMat(smaller);
		}

		// Run detection
		FaceVideoFrame videoFrame = DLIB_DETECTOR.detectFaces(frame);

		if (scaleDown) {
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
		}
		System.out.println("DLIB Duration: " + (System.currentTimeMillis() - start) + " " + videoFrame.hasFaces());
		return videoFrame;
	}

	// private FaceVideoFrame insightfaceDetectFaces(VideoFrame frame) throws IOException, URISyntaxException, InterruptedException {
	// long start = System.currentTimeMillis();
	// Mat original = frame.mat();
	//
	// boolean scaleDown = frame.height() >= DETECTION_SCALE_SIZE + 128;
	// Mat smaller = null;
	// if (scaleDown) {
	// // Resize to 512
	// smaller = original.clone();
	// double aspectRatio = (double) original.height() / (double) original.width();
	// int width = (int) ((double) DETECTION_SCALE_SIZE * aspectRatio);
	// CVUtils.resize(smaller, smaller, DETECTION_SCALE_SIZE, width);
	// frame.setMat(smaller);
	// }
	//
	// // Run detection
	// FaceVideoFrame videoFrame = INSIGHTFACE_DETECTOR.detectFaces(frame);
	//
	// if (scaleDown) {
	// // Reset original and free smaller version
	// videoFrame.setMat(original);
	// frame.setMat(original);
	//
	// for (Face face : videoFrame.faces()) {
	// FaceBox box = face.box();
	//
	// double startXFactor = (double) box.getStartX() / (double) smaller.width();
	// box.setStartX((int) ((double) original.width() * startXFactor));
	//
	// double startYFactor = (double) box.getStartY() / (double) smaller.height();
	// box.setStartY((int) ((double) original.height() * startYFactor));
	//
	// double heightFactor = (double) box.getHeight() / (double) smaller.height();
	// box.setHeight((int) ((double) original.height() * heightFactor));
	//
	// double widthFactor = (double) box.getWidth() / (double) smaller.width();
	// box.setWidth((int) ((double) original.width() * widthFactor));
	//
	// // VideoFrame image = FacedetectorUtils.cropToFace(videoFrame, face);
	// // ImageUtils.show(image.mat());
	// }
	// smaller.release();
	// }
	// System.out.println("InsightFace Duration: " + (System.currentTimeMillis() - start) + " " + videoFrame.hasFaces());
	// return videoFrame;
	// }

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
			if (detectedFaces.size() > 1) {
				logger.warn("I found more than one face in the cropped face image.");
				return;
			}
			if (detectedFaces.size() == 0) {
				logger.warn("Insightfaces did not find face");
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

	private List<FrameWindow> splitWindows(VideoFile video, int windowCount) {
		List<FrameWindow> windows = new ArrayList<>();
		long totalFrames = video.length();
		long startSpace = (long) ((double) totalFrames * 0.03d);
		long endSpace = (long) ((double) totalFrames * 0.03d);
		long spread = totalFrames / (windowCount + 1);
		System.out.println("Total: " + totalFrames);
		System.out.println("Spread: " + spread);
		long from = startSpace;
		int nWindow = 0;
		while (true) {
			from += spread;
			long to = from + spread;
			if (from >= totalFrames) {
				break;
			}
			if (to + endSpace > totalFrames) {
				to = totalFrames - endSpace;
			}
			System.out.println("Window: " + from + " to " + to);
			nWindow++;
			windows.add(new FrameWindow(nWindow, from, to));
			from++;
		}
		System.out.println("Windows: " + windows.size() + " for " + windowCount);
		return windows;
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
		logger.info("Window scan using spread: {} potential window count: {}", (totalFrames / spread), spread);

		int nWindow = 0;
		for (long nFrame = spread; nFrame < totalFrames - spread; nFrame += spread) {
			// logger.info("Scanning frame for potential window " + nFrame);
			video.seekToFrame(nFrame);
			VideoFrame frame = video.frame();
			CVUtils.resize(frame, 512);
			FaceVideoFrame faceFrame = DLIB_DETECTOR.detectFaces(frame);

			// The middle frame of the vindow contains a face - thus add it to the list of windows.
			if (faceFrame.hasFaces()) {
				logger.info("[W" + nWindow + "] Adding window with " + faceFrame.faces().size() + " faces.");
				// DLIB_DETECTOR.markLandmarks(faceFrame);
				// DLIB_DETECTOR.markFaces(faceFrame);
				windows.add(new FrameWindow(nWindow, nFrame, nFrame + spread));
				// viewer.show(faceFrame);
				nWindow++;
			}
		}

		return windows;
	}

}
