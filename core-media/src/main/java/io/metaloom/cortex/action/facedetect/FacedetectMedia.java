package io.metaloom.cortex.action.facedetect;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.LoomMetaType.FS;
import static io.metaloom.cortex.api.media.LoomMetaType.XATTR;

import io.metaloom.cortex.action.facedetect.impl.FaceDetectionType;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.flag.FaceDetectionFlag;

public interface FacedetectMedia extends LoomMedia {

	public static final FaceDetectionType FACE_DETECTION = new FaceDetectionType();

	public static final LoomMetaKey<Integer> FACEDETECT_COUNT_KEY = metaKey("facedetect_count", 1, XATTR, Integer.class);

	public static final LoomMetaKey<FaceDetectionFlag> FACEDETECTION_FLAG_KEY = metaKey("facedetect_flag", 1, XATTR, FaceDetectionFlag.class);

	public static final LoomMetaKey<FaceDetectionResult> FACEDETECTION_RESULT_KEY = metaKey("facedetect_result", 1, FS,
		FaceDetectionResult.class);

	default Integer getFaceCount() {
		return get(FACEDETECT_COUNT_KEY);
	}

	default void setFaceCount(Integer count) {
		put(FACEDETECT_COUNT_KEY, count);
	}

	default FaceDetectionFlag getFacedetectionFlag() {
		return get(FACEDETECTION_FLAG_KEY);
	}

	default void setFacedetectionFlag(FaceDetectionFlag flag) {
		put(FACEDETECTION_FLAG_KEY, flag);
	}

	default FaceDetectionResult getFacedetectionParams() {
		return get(FACEDETECTION_RESULT_KEY);
	}

	default void setFacedetectionParams(FaceDetectionResult result) {
		put(FACEDETECTION_RESULT_KEY, result);
	}

	default boolean hasFacedetectionFlag() {
		return getFacedetectionFlag() != null;
	}
}
