package io.metaloom.cortex.action.captioning;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import io.metaloom.cortex.common.action.media.AbstractMediaTest;
import io.metaloom.video4j.utils.ImageUtils;

public class SmolVLMClientTest extends AbstractMediaTest {

	private SmolVLMClient client = new SmolVLMClient("localhost", 8000);

	@Test
	public void testByURL() throws Exception {
		String result = client.captionByURL("https://shop.manner.com/media/catalog/product/1/7/1700_neapolitaner_grosspkg_18er.jpg");
		System.out.println("Result: " + result);
	}

	@Test
	public void testByBase64Data() throws IOException, URISyntaxException {
		String data = load("/image_base64.dat");
		String result = client.captionByImageData(data);
		System.out.println("Result: " + result);
	}

	@Test
	public void testImageBase64() throws IOException, URISyntaxException {
		BufferedImage image = ImageUtils.load(image1().path().toFile());
		String result = client.captionByImage(image, 512);
		System.out.println(result);
	}

	private String load(String path) throws IOException {
		InputStream ins = SmolVLMClientTest.class.getResourceAsStream(path);
		return IOUtils.toString(ins, Charset.defaultCharset());
	}
}
