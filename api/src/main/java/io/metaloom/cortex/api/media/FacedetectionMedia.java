package io.metaloom.cortex.api.media;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.FS;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.api.media.flag.FaceDetectionFlag;
import io.metaloom.cortex.api.media.param.FaceDetectionParameter;

public interface FacedetectionMedia extends ProcessableMedia {

	public static final LoomMetaKey<Integer> FACEDETECT_COUNT_KEY = metaKey("facedetect_count", 1, XATTR, Integer.class);

	public static final LoomMetaKey<FaceDetectionFlag> FACEDETECTION_FLAG_KEY = metaKey("facedetect_flag", 1, XATTR, FaceDetectionFlag.class);

	public static final LoomMetaKey<FaceDetectionParameter> FACEDETECTION_PARAM_KEY = metaKey("facedetect_param", 1, FS,
		FaceDetectionParameter.class);

	default Integer getFaceCount() {
		return get(FACEDETECT_COUNT_KEY);
	}

	default FacedetectionMedia setFaceCount(Integer count) {
		put(FACEDETECT_COUNT_KEY, count);
		return this;
	}

	default FaceDetectionFlag getFacedetectionFlag() {
		return get(FACEDETECTION_FLAG_KEY);
	}

	default FacedetectionMedia setFacedetectionFlag(FaceDetectionFlag flag) {
		put(FACEDETECTION_FLAG_KEY, flag);
		return this;
	}

	default FaceDetectionParameter getFacedetectionParams() {
		return get(FACEDETECTION_PARAM_KEY);
	}

	default FacedetectionMedia setFacedetectionParams(FaceDetectionParameter params) {
		put(FACEDETECTION_PARAM_KEY, params);
		return this;
	}
}
