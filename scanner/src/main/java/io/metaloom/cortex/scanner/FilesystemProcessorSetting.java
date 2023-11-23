package io.metaloom.cortex.scanner;

import io.metaloom.cortex.api.option.ProcessorSettings;
import io.metaloom.cortex.api.option.action.ConsistencyOptions;
import io.metaloom.cortex.api.option.action.FacedetectOptions;
import io.metaloom.cortex.api.option.action.FingerprintOptions;
import io.metaloom.cortex.api.option.action.HashOptions;
import io.metaloom.cortex.api.option.action.ThumbnailOptions;

public class FilesystemProcessorSetting {

	private ProcessorSettings processorSettings = new ProcessorSettings();

	private ThumbnailOptions thumbnailSettings = new ThumbnailOptions();

	private HashOptions hashSettings = new HashOptions();

	private ConsistencyOptions consistencySettings = new ConsistencyOptions();

	private FingerprintOptions fingerprintActionSettings = new FingerprintOptions();

	private FacedetectOptions facedetectActionSettings = new FacedetectOptions();

	public HashOptions getHashSettings() {
		return hashSettings;
	}

	public FilesystemProcessorSetting setHashSettings(HashOptions hashSettings) {
		this.hashSettings = hashSettings;
		return this;
	}

	public ConsistencyOptions getConsistencySettings() {
		return consistencySettings;
	}

	public FilesystemProcessorSetting setConsistencySettings(ConsistencyOptions consistencySettings) {
		this.consistencySettings = consistencySettings;
		return this;
	}

	public FacedetectOptions getFacedetectActionSettings() {
		return facedetectActionSettings;
	}

	public FilesystemProcessorSetting setFacedetectActionSettings(FacedetectOptions facedetectActionSettings) {
		this.facedetectActionSettings = facedetectActionSettings;
		return this;
	}

	public FingerprintOptions getFingerprintActionSettings() {
		return fingerprintActionSettings;
	}

	public FilesystemProcessorSetting setFingerprintActionSettings(FingerprintOptions fingerprintActionSettings) {
		this.fingerprintActionSettings = fingerprintActionSettings;
		return this;
	}

	public ThumbnailOptions getThumbnailSettings() {
		return thumbnailSettings;
	}

	public FilesystemProcessorSetting setThumbnailSettings(ThumbnailOptions thumbnailSettings) {
		this.thumbnailSettings = thumbnailSettings;
		return this;
	}

	public ProcessorSettings getProcessorSettings() {
		return processorSettings;
	}

	public FilesystemProcessorSetting setProcessorSettings(ProcessorSettings processorSettings) {
		this.processorSettings = processorSettings;
		return this;
	}
}
