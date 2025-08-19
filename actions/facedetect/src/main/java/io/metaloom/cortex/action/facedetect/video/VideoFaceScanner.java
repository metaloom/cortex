package io.metaloom.cortex.action.facedetect.video;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.facedetection.client.FaceDetectionServerClient;
import io.metaloom.facedetection.client.model.DetectionResponse;
import io.metaloom.facedetection.client.model.FaceModel;
import io.metaloom.video.facedetect.FaceVideoFrame;
import io.metaloom.video.facedetect.Facedetector;
import io.metaloom.video.facedetect.FacedetectorUtils;
import io.metaloom.video.facedetect.face.Face;
import io.metaloom.video.facedetect.face.FaceBox;
import io.metaloom.video.facedetect.inspireface.InspireFacedetector;
import io.metaloom.video4j.VideoFile;
import io.metaloom.video4j.VideoFrame;
import io.metaloom.video4j.opencv.CVUtils;
import io.metaloom.video4j.utils.ImageUtils;
import io.metaloom.video4j.utils.SimpleImageViewer;

@Singleton
public class VideoFaceScanner {

	private final static Logger logger = LoggerFactory.getLogger(VideoFaceScanner.class);

	/**
	 * Size to which the frame should be scaled down to before running initial face detection
	 */
	public static final int DETECTION_SCALE_SIZE = 512;

	public static final int WINDOW_STEPS = 15;

	private final InspireFacedetector inspireface;
	private SimpleImageViewer viewer = new SimpleImageViewer();

	@Inject
	public VideoFaceScanner(InspireFacedetector inspireface) {
		this.inspireface = inspireface;
	}

	public VideoFaceScannerReport scan(VideoFile video, int maxWindowCount)
		throws InterruptedException, IOException, URISyntaxException {
		VideoFaceScannerReport report = new VideoFaceScannerReport();

		// Locate potential windows
		// List<FrameWindow> windows = identifyPotentialWindows(video, windowCount);
		List<FrameWindow> windows = VideoFaceScannerUtils.splitWindows(video.length(), WINDOW_STEPS, maxWindowCount);
		report.setWindowInfo(maxWindowCount, windows.size());
		logger.info("Split Window: {} windows to be scanned.", windows.size());

		// Process the windows and locate the best faces
		List<VideoFace> allFaces = new ArrayList<>();
		for (FrameWindow window : windows) {
			allFaces.addAll(processWindow(video, window));
		}

		// Now process all found faces
		try {
			report.setFaces(processFaces(allFaces));
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Got total of {} faces with embeddings in {} windows", allFaces.size(), windows.size());
		return report;
	}

	private List<VideoFace> processFaces(List<VideoFace> faces) {
		faces = faces.stream().sorted(this::blurComperator).toList();
		for (VideoFace face : faces) {
			System.out.println(face.getBlurriness());
		}
		List<VideoFace> output = new ArrayList<>();
		for (VideoFace face : faces) {
			// processFace(face);
			if (face.hasEmbedding()) {
				output.add(face);
			}
			if (output.size() >= 5) {
				break;
			}
		}

		// double blurriness = 0;
		// for (VideoFace face : faces) {
		// blurriness += face.getBlurriness();
		// }
		// double avgBlurriness = blurriness / (double)faces.size();
		// System.out.println(avgBlurriness);
		// while(faces.size() > 5) {
		// faces.stream().sorted(this::blurComperator).limit(faces.size()-1);
		// }
		return output;
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
	public List<VideoFace> processWindow(VideoFile video, FrameWindow window)
		throws InterruptedException, IOException, URISyntaxException {
		if (logger.isDebugEnabled()) {
			logger.debug("Tuning window: {}", window);
		}

		List<VideoFace> faces = scanWindow(video, window, WINDOW_STEPS);
		// Only keep the sharpest face from the window
		// faces = faces.stream().sorted(this::blurComperator).limit(1).toList();
		logger.info("Window scan of window {} yield {} faces", window, faces.size());
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
	private List<VideoFace> scanWindow(VideoFile video, FrameWindow window, int windowSteps)
		throws IOException, URISyntaxException, InterruptedException {

		int nWindow = window.number();
		long from = window.from();
		long to = window.to();

		List<VideoFace> faces = new ArrayList<>();
		long start = System.currentTimeMillis();
		long nFrame = from;
		logger.info("Scanning window {} to {} with steps {}", from, to, windowSteps);
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

			// Stop processing when we found n faces
			if (faces.size() > 5) {
				// System.out.println("Got enough faces");
				break;
			}

			List<VideoFace> dFaces = processFrame(frame);
			faces.addAll(dFaces);

			// No faces found. Lets skip a few frames
			if (dFaces.isEmpty()) {
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
		return faces;
	}

	private List<VideoFace> processFrame(VideoFrame frame) {
		List<VideoFace> faces = new ArrayList<>();
		// FaceVideoFrame faceFrame = dlibDetectFaces(frame);
		FaceVideoFrame faceFrame = detectFaces(inspireface, frame);

		if (faceFrame != null && faceFrame.hasFaces()) {
			// DLIB_DETECTOR.markFaces(frame);
			// DLIB_DETECTOR.markLandmarks(frame);
			// viewer2.show(frame);

			// Process each found face
			for (Face face : faceFrame.faces()) {
				VideoFace videoFace = new VideoFace(face);

				videoFace.setFrame(faceFrame.number());
				// Crop to the face and calculate the blurriness
				// System.out.println("Crop to: " + face + " from " + faceFrame.height() + " x " + faceFrame.width());

				// Mat faceImage = faceFrame.mat().clone();
				int padding = (int) ((double) face.box().getWidth() * 1d);
				Mat faceImage = FacedetectorUtils.cropToFace(faceFrame.mat(), face, padding);
				viewer.show(faceImage);
				// ImageUtils.show(faceImage);
				double blurriness = CVUtils.blurriness(faceImage);
				// if (blurriness < BLUR_THRESHOLD) {
				// logger.warn("Omitting face due to blur check: {}", blurriness);
				// Skipped due to bad quality
				// continue;
				// }
				videoFace.setBlurriness(blurriness);
				BufferedImage croppedFaceImage = ImageUtils.matToBufferedImage(faceImage);
				// System.out.println("Cropped: " + croppedFaceImage.getWidth() + " x " + croppedFaceImage.getHeight());
				// croppedFaceImage = ImageUtils.scale(croppedFaceImage, croppedFaceImage.getWidth()*2, croppedFaceImage.getHeight()*2);
				// BufferedImage croppedFaceImage = ImageUtils.matToBufferedImage(frame.mat());
				videoFace.setImage(croppedFaceImage);
				// faceImage.release();
				// logger.info("Adding face for window " + nWindow + " now " + faces.size() + " in total for this window.");
				faces.add(videoFace);
			}

		}
		return faces;

	}

	public FaceVideoFrame detectFaces(Facedetector detector, VideoFrame frame) {
		long start = System.currentTimeMillis();
		Mat original = frame.mat();

		// boolean scaleDown = frame.height() >= DETECTION_SCALE_SIZE + 128;
		boolean scaleDown = false;
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
		FaceVideoFrame videoFrame = detector.detectFaces(frame);

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
		System.out.println("Inspireface Duration: " + (System.currentTimeMillis() - start) + " " + videoFrame.hasFaces());
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

	private void processFaceWithClient(FaceDetectionServerClient client, Face dFace) {
		BufferedImage croppedFaceImage = dFace.get("image");
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
				dFace.setEmbedding(face.getEmbedding());
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
		logger.info("Window scan using spread: {} potential window count: {}", (totalFrames / spread), spread);

		int nWindow = 0;
		for (long nFrame = spread; nFrame < totalFrames - spread; nFrame += spread) {
			// logger.info("Scanning frame for potential window " + nFrame);
			video.seekToFrame(nFrame);
			VideoFrame frame = video.frame();
			CVUtils.resize(frame, 512);
			FaceVideoFrame faceFrame = inspireface.detectFaces(frame);

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

	private int blurComperator(VideoFace f1, VideoFace f2) {
		double b1 = f1.getBlurriness();
		double b2 = f2.getBlurriness();
		return Double.compare(b2, b1);
	}

}
