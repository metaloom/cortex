package io.metaloom.cortex.action.captioning;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.imgscalr.Scalr;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import io.metaloom.video4j.utils.ImageUtils;
import io.vertx.core.json.JsonObject;

public class SmolVLMClient {

	private final String host;
	private final int port;

	private String prompt = null;

	public SmolVLMClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String captionByImageData(String base64) throws URISyntaxException {
		JsonObject json = new JsonObject();
		if (prompt != null) {
			json.put("prompt", prompt);
		}
		json.put("image_data", base64);
		return caption(json);
	}

	public String captionByURL(String url) throws URISyntaxException {
		JsonObject json = new JsonObject();
		if (prompt != null) {
			json.put("prompt", prompt);
		}
		json.put("image_url", url);
		return caption(json);
	}

	private String caption(JsonObject json) throws URISyntaxException {
		URI uri = new URI("http://" + host + ":" + port + "/caption");
		String jsonStr = json.encode();
		HttpRequest request = HttpRequest.newBuilder()
			.uri(uri)
			.header("Content-Type", "application/json")
			.POST(HttpRequest.BodyPublishers.ofString(jsonStr))
			.build();

		// It is important to switch to HTTP_1_1. Otherwise FastAPI will not accept the request.
		HttpClient client = HttpClient.newBuilder()
			.version(Version.HTTP_1_1).build();

		try {
			// Send the request and get the response
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			// Print response status and body
			System.out.println("Status Code: " + response.statusCode());
			System.out.println("Response Body: " + response.body());

			// HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			// return response.body();
			return response.body();
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	public String captionByImage(BufferedImage image, int targetSize) throws URISyntaxException, IOException {
		BufferedImage lowRes = Scalr.resize(image, targetSize);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		// Save the resized image as JPG into the stream
		try (Base64OutputStream os = new Base64OutputStream(bos)) {
			ImageUtils.saveJPG(os, lowRes);
		}

		// Get the encoded bytes from the ByteArrayOutputStream
		byte[] encodedBytes = bos.toByteArray();

		// Convert the encoded bytes to a string
		String encodedString = new String(encodedBytes, StandardCharsets.UTF_8);

		return captionByImageData(encodedString);
	}
}
