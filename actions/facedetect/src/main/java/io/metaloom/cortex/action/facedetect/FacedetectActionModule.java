package io.metaloom.cortex.action.facedetect;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import io.metaloom.cortex.api.action.CortexAction;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractActionModule;
import io.metaloom.cortex.common.dlib.DLibModelProvisioner;
import io.metaloom.cortex.common.option.CortexActionOptionDeserializerInfo;
import io.metaloom.facedetection.client.FaceDetectionServerClient;
import io.metaloom.video.facedetect.dlib.DLibFacedetector;
import io.metaloom.video.facedetect.dlib.impl.DLibFacedetectorImpl;
import io.metaloom.video.facedetect.inspireface.InspireFacedetector;

@Module
public abstract class FacedetectActionModule extends AbstractActionModule {

	public static final Logger log = LoggerFactory.getLogger(FacedetectActionModule.class);

	public static final String FACE_DETECT_SERVER_BASEURL = "http://cortex:8010/api/v1";

	@Binds
	@IntoSet
	abstract CortexAction bindAction(FacedetectAction action);

	@IntoSet
	@Provides
	public static CortexActionOptionDeserializerInfo optionInfo() {
		return new CortexActionOptionDeserializerInfo(FacedetectActionOptions.class, FacedetectActionOptions.KEY);
	}

	@Provides
	@Singleton
	public static FacedetectActionOptions options(CortexOptions options) {
		return actionOptions(options, FacedetectActionOptions.KEY, new FacedetectActionOptions());
	}

	@Provides
	@Singleton
	public static FaceDetectionServerClient detectionClient(FacedetectActionOptions options) {
		FaceDetectionServerClient client = FaceDetectionServerClient.newBuilder()
			.setBaseURL(FACE_DETECT_SERVER_BASEURL).build();
		return client;

	}

	@Provides
	@Singleton
	public static DLibFacedetector dlibDetector(FacedetectActionOptions options) {
		if (!options.getCapabilities().contains(FacedetectActionCapabilities.DLIB)) {
			return null;
		}
		try {
			DLibModelProvisioner.extractModelData(Paths.get("dlib"));
			// DLIB_DETECTOR = DLibFacedetector.create();
			// DLIB_DETECTOR.setMinFaceHeightFactor(0.01f);
			// DLIB_DETECTOR.enableCNNDetector();
			return new DLibFacedetectorImpl();
		} catch (IOException e) {
			throw new RuntimeException("Failed to extract dlib models", e);
		}
	}

	@Provides
	@Singleton
	public static InspireFacedetector inspirefaceDetector(FacedetectActionOptions options) {
		if (!options.getCapabilities().contains(FacedetectActionCapabilities.INSPIREFACE)) {
			return null;
		}
		try {
			String packPath = options.getInspirefacePackPath();
			InspireFacedetector detector = InspireFacedetector.create(packPath, 640, true, true, true);
			detector.setMinFaceHeightFactor(options.getMinFaceHeightFactor());
			return detector;
		} catch (FileNotFoundException e) {
			log.error("Failed to load dlib", e);
			throw new RuntimeException(e);
		}
	}

}
