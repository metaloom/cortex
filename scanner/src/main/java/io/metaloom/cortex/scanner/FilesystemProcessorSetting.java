package io.metaloom.cortex.scanner;

import io.metaloom.cortex.action.common.settings.ProcessorSettings;
import io.metaloom.cortex.action.fp.FingerprintActionSettings;
import io.metaloom.cortex.action.hash.HashActionSettings;
import io.metaloom.loom.cortex.action.consistency.ConsistencyActionSettings;
import io.metaloom.loom.cortex.action.facedetect.FacedetectActionSettings;
import io.metaloom.loom.cortex.action.thumbnail.ThumbnailActionSettings;

public class FilesystemProcessorSetting {

	private ProcessorSettings processorSettings = new ProcessorSettings();

	private ThumbnailActionSettings thumbnailSettings = new ThumbnailActionSettings();

	private HashActionSettings hashSettings = new HashActionSettings();

	private ConsistencyActionSettings consistencySettings = new ConsistencyActionSettings();

	private FingerprintActionSettings fingerprintActionSettings = new FingerprintActionSettings();

	private FacedetectActionSettings facedetectActionSettings = new FacedetectActionSettings();

	public HashActionSettings getHashSettings() {
		return hashSettings;
	}

	public FilesystemProcessorSetting setHashSettings(HashActionSettings hashSettings) {
		this.hashSettings = hashSettings;
		return this;
	}

	public ConsistencyActionSettings getConsistencySettings() {
		return consistencySettings;
	}

	public FilesystemProcessorSetting setConsistencySettings(ConsistencyActionSettings consistencySettings) {
		this.consistencySettings = consistencySettings;
		return this;
	}

	public FacedetectActionSettings getFacedetectActionSettings() {
		return facedetectActionSettings;
	}

	public FilesystemProcessorSetting setFacedetectActionSettings(FacedetectActionSettings facedetectActionSettings) {
		this.facedetectActionSettings = facedetectActionSettings;
		return this;
	}

	public FingerprintActionSettings getFingerprintActionSettings() {
		return fingerprintActionSettings;
	}

	public FilesystemProcessorSetting setFingerprintActionSettings(FingerprintActionSettings fingerprintActionSettings) {
		this.fingerprintActionSettings = fingerprintActionSettings;
		return this;
	}

	public ThumbnailActionSettings getThumbnailSettings() {
		return thumbnailSettings;
	}

	public FilesystemProcessorSetting setThumbnailSettings(ThumbnailActionSettings thumbnailSettings) {
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
