package io.metaloom.loom.cortex.action.facedetect;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.avro.util.ByteBufferOutputStream;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;

import io.metaloom.loom.cortex.action.facedetect.avro.Facedetection;
import io.metaloom.loom.cortex.action.facedetect.avro.FacedetectionBox;
import io.metaloom.utils.ByteBufferUtils;
import io.metaloom.video4j.utils.ImageUtils;

public class FacedetectionTest {

	@Test
	public void testImage() throws IOException {
		BufferedImage image = ImageUtils.loadResource("/pexels-photo-3812743_512.jpeg");
		ByteBufferOutputStream os = new ByteBufferOutputStream();
		ImageUtils.saveJPG(os, image);
		Facedetection f = Facedetection.newBuilder()
			.setAssetHash("dummy")
			.setFrame(10)
			.setBox(FacedetectionBox.newBuilder().setHeight(10).setWidth(10).setStartX(10).setStartY(10).build())
			.setThumbnail(ByteBufferUtils.convertToOne(os.getBufferList()))
			.build();

		try (InputStream ins = new ByteBufferBackedInputStream(f.getThumbnail())) {
			BufferedImage img = ImageIO.read(ins);
			assertNotNull(img);
		}
	}
}
