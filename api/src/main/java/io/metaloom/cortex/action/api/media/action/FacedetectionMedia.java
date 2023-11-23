package io.metaloom.cortex.action.api.media.action;

import static io.metaloom.cortex.action.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.action.api.media.LoomMetaType.FS;
import static io.metaloom.cortex.action.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.action.api.media.LoomMetaKey;
import io.metaloom.cortex.action.api.media.ProcessableMedia;
import io.metaloom.cortex.action.api.media.flag.FaceDetectionFlags;
import io.metaloom.cortex.action.api.media.param.FaceDetectionParameters;

public interface FacedetectionMedia extends ProcessableMedia {

	public static final LoomMetaKey<Integer> FACEDETECT_COUNT_KEY = metaKey("facedetect_count", 1, XATTR, Integer.class);

	public static final LoomMetaKey<FaceDetectionFlags> FACEDETECTION_FLAGS_KEY = metaKey("facedetect_flags", 1, XATTR, FaceDetectionFlags.class);

	public static final LoomMetaKey<FaceDetectionParameters> FACEDETECTION_PARAMS_KEY = metaKey("facedetect_params", 1, FS,
		FaceDetectionParameters.class);

	default Integer getFaceCount() {
		return get(FACEDETECT_COUNT_KEY);
	}

	default FacedetectionMedia setFaceCount(Integer count) {
		put(FACEDETECT_COUNT_KEY, count);
		return this;
	}

	default FaceDetectionFlags getFacedetectionFlags() {
		return get(FACEDETECTION_FLAGS_KEY);
	}

	default FacedetectionMedia setFacedetectionFlags(FaceDetectionFlags flag) {
		put(FACEDETECTION_FLAGS_KEY, flag);
		return this;
	}

	default FaceDetectionParameters getFacedetectionParams() {
		return get(FACEDETECTION_PARAMS_KEY);
	}

	default FacedetectionMedia setFacedetectionParams(FaceDetectionParameters params) {
		put(FACEDETECTION_PARAMS_KEY, params);
		return this;
	}
}
