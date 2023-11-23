package io.metaloom.cortex.api.action.media;

import io.metaloom.cortex.api.action.media.action.ChunkMedia;
import io.metaloom.cortex.api.action.media.action.FacedetectionMedia;
import io.metaloom.cortex.api.action.media.action.FingerprintMedia;
import io.metaloom.cortex.api.action.media.action.HashMedia;
import io.metaloom.cortex.api.action.media.action.OCRMEdia;
import io.metaloom.cortex.api.action.media.action.SceneDetectionMedia;
import io.metaloom.cortex.api.action.media.action.ThumbnailMedia;
import io.metaloom.cortex.api.action.media.action.TikaMedia;

public interface LoomMedia extends HashMedia, FacedetectionMedia, ThumbnailMedia, FingerprintMedia, OCRMEdia, ChunkMedia, TikaMedia, SceneDetectionMedia {

}
