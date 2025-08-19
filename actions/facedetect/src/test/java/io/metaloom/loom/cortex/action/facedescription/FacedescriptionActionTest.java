package io.metaloom.loom.cortex.action.facedescription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.metaloom.cortex.action.facedescription.FaceDescription;
import io.metaloom.cortex.action.facedescription.FacedescriptionAction;
import io.metaloom.cortex.action.facedetect.FacedetectActionOptions;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.media.LoomClientMock;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.client.common.LoomClientException;
import io.metaloom.loom.cortex.action.facedetect.AbstractFacedetectMediaTest;
import io.metaloom.video4j.utils.ImageUtils;

public class FacedescriptionActionTest extends AbstractFacedetectMediaTest {

	@Test
	public void testProcessMedia() throws FileNotFoundException, IOException, LoomClientException {
		LoomMedia video = mediaVideo1();
		mockAction().process(video);
	}

	@Test
	public void testProcessImage() throws IOException, LoomClientException {
		BufferedImage image = ImageUtils.loadResource("/images/face_occluded.jpg");
		for (int i = 0; i < 10; i++) {
			FaceDescription result = mockAction().processFace(image);
			assertTrue(result.getAge() > 18 && result.getAge() < 30);
			assertEquals("caucasian", result.getRace());
			assertEquals("closed", result.getMouth());
			assertEquals("open", result.getEyes());
			assertEquals("female", result.getGender());
			assertFalse(result.isNsfw());
			assertTrue(result.isFrontal());
			assertFalse(result.isProfile());
			assertTrue(result.isOccluded());
		}
	}

	public FacedescriptionAction mockAction() throws FileNotFoundException, LoomClientException {
		LoomClient client = LoomClientMock.mockClient();
		FacedetectActionOptions option = new FacedetectActionOptions();
		option.setMinFaceHeightFactor(0.05f).setVideoScaleSize(512);
		ObjectMapper mapper = new ObjectMapper();
		return new FacedescriptionAction(client, new CortexOptions(), option, mapper);
	}
}
