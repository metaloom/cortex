package io.metaloom.cortex.action.facedetect.video;

import java.util.ArrayList;
import java.util.List;

public final class VideoFaceScannerUtils {

	private VideoFaceScannerUtils() {
	}

	public static List<FrameWindow> splitWindows(long totalFrames, int windowSteps, int maxWindowCount) {
		List<FrameWindow> windows = new ArrayList<>();
		long startSpace = (long) ((double) totalFrames * 0.05d);
		long endSpace = (long) ((double) totalFrames * 0.05d);
		long spread = totalFrames / maxWindowCount;

		while (spread < windowSteps * 10) {
			if (maxWindowCount <= 1) {
				break;
			}
			maxWindowCount--;
			spread = totalFrames / maxWindowCount;
		}

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
			nWindow++;
			windows.add(new FrameWindow(nWindow, from, to));
			from++;
		}
		if (windows.isEmpty()) {
			windows.add(new FrameWindow(0, 0, totalFrames));
		}
		return windows;
	}

}
