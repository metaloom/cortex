package io.metaloom.loom.cortex.action.facedetect;

import static io.metaloom.cortex.api.action.media.action.HashMedia.SHA_512_KEY;
import static io.metaloom.loom.test.assertj.LoomWorkerAssertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.action.media.AbstractMediaTest;
import io.metaloom.cortex.action.media.LoomClientMock;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public class FacedetectActionTest extends AbstractMediaTest {

	private FacedetectAction action;

	@BeforeEach
	public void setupAction() throws IOException {
		action = mockAction();
	}

	@Test
	public void testVideo() throws IOException {
		LoomMedia media = sampleVideoMedia2();
		ActionResult result = action.process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(1).hasXAttr(SHA_512_KEY);
		System.out.println("Faces: " + media.getFaceCount());
	}

	@Test
	public void testImage() throws IOException {
		FacedetectAction action = mockAction();
		LoomMedia media = sampleImageMedia1();
		ActionResult result = action.process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasXAttr(1).hasXAttr(SHA_512_KEY);
		System.out.println("Faces: " + media.getFaceCount());
	}

	public FacedetectAction mockAction() throws FileNotFoundException {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		FacedetectOptions option = new FacedetectOptions();
		option.setMinFaceHeightFactor(0.05f).setVideoScaleSize(512);
		return new FacedetectAction(client, new CortexOptions(), option);
	}
}
