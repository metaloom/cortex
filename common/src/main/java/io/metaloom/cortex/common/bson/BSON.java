package io.metaloom.cortex.common.bson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.undercouch.bson4jackson.BsonFactory;

public class BSON {

	private static final ObjectMapper MAPPER;

	static {
		MAPPER = new ObjectMapper(new BsonFactory());
	}

	public static byte[] writeValue(Object value) {
		try {
			return MAPPER.writeValueAsBytes(value);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeValue(FileOutputStream fos, Object value) {
		try {
			MAPPER.writeValue(fos, value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T readValue(InputStream ins, Class<T> classOfT) {
		try {
			return MAPPER.readValue(ins, classOfT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T readValue(ByteBuffer buffer, Class<T> classOfT) {
		try {
			return MAPPER.readValue(buffer.array(), classOfT);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
