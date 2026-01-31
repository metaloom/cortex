package io.metaloom.cortex.action.whisper;
import static org.bytedeco.ffmpeg.global.avutil.AV_SAMPLE_FMT_FLT;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

public class AudioExtractor {

	public static void decodeAudioToPCM(File mediaPath, Consumer<AudioChunk> audioChunkConsumer) throws Exception {

		try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(mediaPath)) {

			grabber.setAudioChannels(1);
			grabber.setSampleRate(16000);
			grabber.setSampleFormat(AV_SAMPLE_FMT_FLT);

			grabber.start();

			List<float[]> chunks = new ArrayList<>();
			int totalSamples = 0;
			int silenceCounter = 0;
			int silenceMinSamples = 2000;
			Frame frame;
			while ((frame = grabber.grabSamples()) != null) {
				if (frame.samples == null) {
					continue;
				}

				long ts = frame.timestamp;
				FloatBuffer fb = (FloatBuffer) frame.samples[0];
				float[] chunk = new float[fb.remaining()];
				fb.get(chunk);

				chunks.add(chunk);
				totalSamples += chunk.length;

				if (isSilentRMS(chunk, 0.02f)) {
					silenceCounter += chunk.length;
				} else {
					silenceCounter = 0; // reset silence counter when audio is detected
				}

				if (silenceCounter >= silenceMinSamples && totalSamples > 30_000) {
					System.err.println("Silence! " + silenceCounter);
					audioChunkConsumer.accept(new AudioChunk(concat(chunks, totalSamples)));
					chunks.clear();
					totalSamples = 0;
					continue;
				}

			}

			grabber.stop();

		}
	}

	private static boolean isSilentRMS(float[] samples, float threshold) {
		double sum = 0;
		for (float s : samples) {
			sum += s * s;
		}
		double rms = Math.sqrt(sum / samples.length);
		return rms < threshold;
	}

	// 0.02
	private static float[] concat(List<float[]> chunks, int totalSamples) {
		float[] pcm = new float[totalSamples];
		int offset = 0;
		for (float[] c : chunks) {
			System.arraycopy(c, 0, pcm, offset, c.length);
			offset += c.length;
		}
		return pcm;
	}

	private static boolean isSilent(float[] samples, float threshold) {
		float max = 0;
		for (float s : samples) {
			max = Math.max(max, Math.abs(s));
		}
		return max < threshold;
	}
}
