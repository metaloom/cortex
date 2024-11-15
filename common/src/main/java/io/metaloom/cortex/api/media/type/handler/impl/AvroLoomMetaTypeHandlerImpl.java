package io.metaloom.cortex.api.media.type.handler.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericContainer;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.type.LoomMetaCoreType;
import io.metaloom.cortex.api.media.type.LoomMetaType;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.utils.fs.FileUtils;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.SHA512;

@Singleton
public class AvroLoomMetaTypeHandlerImpl implements LoomMetaTypeHandler {

	private final CortexOptions options;
	private Path basePath;

	@Inject
	public AvroLoomMetaTypeHandlerImpl(CortexOptions options) {
		this.options = options;
		this.basePath = options.getMetaPath();
		try {
			FileUtils.ensureParentFolder(basePath);
		} catch (Exception e) {
			throw new RuntimeException("Failed to verify basePath " + basePath, e);
		}
	}

	@Override
	public LoomMetaType type() {
		return LoomMetaCoreType.AVRO;
	}

	@Override
	public <T> void put(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		DatumWriter<T> datumWriter = new SpecificDatumWriter<>(metaKey.getValueClazz());
		if (value instanceof GenericContainer gc) {

			File file = toMetaPath(media, metaKey).toFile();
			try (DataFileWriter<T> dataFileWriter = new DataFileWriter<>(datumWriter)) {
				// dataFileWriter.appendTo(file);
				dataFileWriter.create(gc.getSchema(), file);
				dataFileWriter.append(value);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("The provided value is not compatible with this avro type handler.");
		}
	}

	@Override
	public <T> T get(LoomMedia media, LoomMetaKey<T> metaKey) {
		DatumReader<T> datumReader = new SpecificDatumReader<>(metaKey.getValueClazz());
		File file = toMetaPath(media, metaKey).toFile();
		if (!file.exists()) {
			return null;
		}
		try (DataFileReader<T> dataFileReader = new DataFileReader<>(file, datumReader)) {
			T item = null;
			item = dataFileReader.next();
			// Read the movie record from the file
			// if (dataFileReader.hasNext()) {
			// movie = dataFileReader.next();
			// }
			return item;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		return toMetaPath(media, metaKey).toFile().exists();
	}

	// protected File getFileForHash(SHA512 hash) {
	// File avroStorage = new File(basePath.toFile(), "avro");
	//
	// return new File(avroStorage, hash.toString() + ".avro");
	// }

	protected <T> Path toMetaPath(LoomMedia media, LoomMetaKey<T> key) {
		SHA512 hash = media.getSHA512();
		String fileName = hash + ".meta";
		Path basePath = options.getMetaPath().resolve(key.key());
		System.out.println("Checking path: " + basePath + " " + basePath.toFile().exists());
		Path dirPath = HashUtils.segmentPath(basePath, hash);
		Path filePath = dirPath.resolve(fileName);
		try {
			FileUtils.ensureParentFolder(filePath);
		} catch (IOException e) {
			throw new RuntimeException("Failed to ensure folder structure for metadata exists", e);
		}
		return filePath;
	}

}
