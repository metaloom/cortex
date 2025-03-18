package io.metaloom.cortex.action.facedetect;

import static io.metaloom.cortex.api.media.LoomMetaKey.metaKey;
import static io.metaloom.cortex.api.media.type.LoomMetaCoreType.AVRO;
import static io.metaloom.cortex.api.media.type.LoomMetaCoreType.XATTR;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.flag.FaceDetectionFlag;
import io.metaloom.cortex.media.facedetect.impl.FaceDetectionType;
import io.metaloom.loom.cortex.action.facedetect.avro.Facedetection;

public interface FacedetectMedia extends LoomMedia {

	public static final FaceDetectionType FACE_DETECTION = new FaceDetectionType();

	/**
	 * XAttr which stores the count of detected faces.
	 */
	public static final LoomMetaKey<Integer> FACEDETECT_COUNT_KEY = metaKey("facedetect_count", 1, XATTR, Integer.class);

	/**
	 * XAttr which stores the state of the fact detection process.
	 */
	public static final LoomMetaKey<FaceDetectionFlag> FACEDETECTION_FLAG_KEY = metaKey("facedetect_flag", 1, XATTR, FaceDetectionFlag.class);

	public static final LoomMetaKey<Facedetection> FACEDETECTION_RESULT_KEY = metaKey("facedetect_result", 1, AVRO, Facedetection.class);

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

	default Facedetection getFacedetectionParams() {
		return get(FACEDETECTION_RESULT_KEY);
	}

	default void appendFacedetection(Facedetection result) {
		append(FACEDETECTION_RESULT_KEY, result);
	}

	default boolean hasFacedetectionFlag() {
		return getFacedetectionFlag() != null;
	}
}
