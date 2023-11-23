package io.metaloom.cortex.action.api.media;

import io.metaloom.cortex.action.api.media.action.ChunkMedia;
import io.metaloom.cortex.action.api.media.action.FacedetectionMedia;
import io.metaloom.cortex.action.api.media.action.FingerprintMedia;
import io.metaloom.cortex.action.api.media.action.HashMedia;
import io.metaloom.cortex.action.api.media.action.OCRMEdia;
import io.metaloom.cortex.action.api.media.action.ThumbnailMedia;
import io.metaloom.cortex.action.api.media.action.TikaMedia;

public interface LoomMedia extends HashMedia, FacedetectionMedia, ThumbnailMedia, FingerprintMedia, OCRMEdia, ChunkMedia, TikaMedia {

}
