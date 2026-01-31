package io.metaloom.cortex.action.whisper;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.ggerganov.whispercpp.WhisperCpp;
import io.github.ggerganov.whispercpp.bean.WhisperSegment;
import io.github.ggerganov.whispercpp.params.WhisperFullParams;
import io.github.ggerganov.whispercpp.params.WhisperSamplingStrategy;

@Singleton
public class WhisperMediaProcessor {

	private WhisperOptions options;

	@Inject
	public WhisperMediaProcessor(WhisperOptions options) {
		this.options = options;
	}
	
	public void process(File mediaFile) throws Exception {

		WhisperCpp whisper = new WhisperCpp();
		try {
			// By default, models are loaded from ~/.cache/whisper/ and are usually named "ggml-${name}.bin"
			// or you can provide the absolute path to the model file.
			whisper.initContext(options.getModelPath());
			WhisperFullParams.ByValue whisperParams = whisper.getFullDefaultParams(WhisperSamplingStrategy.WHISPER_SAMPLING_BEAM_SEARCH);

			// custom configuration if required
			// whisperParams.n_threads = 8;
			whisperParams.temperature = options.getTemperature();
			whisperParams.temperature_inc =   options.getTemperatureInc();
			whisperParams.language = options.getLanguage();

			AudioExtractor.decodeAudioToPCM(mediaFile, ac -> {
				try {
					List<WhisperSegment> whisperSegmentList = whisper.fullTranscribeWithTime(whisperParams, ac.getAudio());
					for (WhisperSegment whisperSegment : whisperSegmentList) {
						long start = whisperSegment.getStart();
						long end = whisperSegment.getEnd();

						String text = whisperSegment.getSentence();

						System.out.println("start: " + start);
						System.out.println("end: " + end);
						System.out.println("text: " + text);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			whisper.close();
		}

	}

}
