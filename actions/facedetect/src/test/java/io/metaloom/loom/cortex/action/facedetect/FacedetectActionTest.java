package io.metaloom.loom.cortex.action.facedetect;

import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;
import static io.metaloom.cortex.media.facedetect.FacedetectMedia.FACE_DETECTION;
import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.facedetect.FacedetectAction;
import io.metaloom.cortex.action.facedetect.FacedetectActionOptions;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.cortex.common.action.media.LoomClientMock;
import io.metaloom.cortex.media.facedetect.FacedetectMedia;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class FacedetectActionTest extends AbstractMediaTest {

	private FacedetectAction action;

	@BeforeEach
	public void setupAction() throws IOException {
		action = mockAction();
	}

	@Test
	public void testVideo() throws IOException {
		FacedetectMedia media = mediaVideo2().of(FACE_DETECTION);
		ActionResult result = action.process(ctx(media));
		assertThat(result).isSuccess();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512_KEY, FacedetectMedia.FACEDETECT_COUNT_KEY);
		assertTrue(media.getFaceCount() > 10, "There should be at least 10 detections.");
	}

	@Test
	public void testImage() throws IOException {
		FacedetectAction action = mockAction();
		FacedetectMedia media = mediaImage1().of(FACE_DETECTION);
		ActionResult result = action.process(ctx(media));
		assertThat(result).isSuccess();
		assertThat(media).hasXAttr(1).hasXAttr(SHA_512_KEY);
		System.out.println("Faces: " + media.getFaceCount());
	}

	public FacedetectAction mockAction() throws FileNotFoundException {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		FacedetectActionOptions option = new FacedetectActionOptions();
		option.setMinFaceHeightFactor(0.05f).setVideoScaleSize(512);
		return new FacedetectAction(client, new CortexOptions(), option);
	}
}
