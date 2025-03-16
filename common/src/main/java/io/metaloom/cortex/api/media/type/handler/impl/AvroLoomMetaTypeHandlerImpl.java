package io.metaloom.cortex.api.media.type.handler.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
			Objects.requireNonNull(basePath, "The metaPath was not set in the configuration");
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
	public <T> void append(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		DatumWriter<T> datumWriter = new SpecificDatumWriter<>(metaKey.getValueClazz());
		if (value instanceof GenericContainer gc) {

			File file = toMetaPath(media, metaKey).toFile();
			boolean exists = file.exists();
			try (DataFileWriter<T> dataFileWriter = new DataFileWriter<>(datumWriter)) {
				if (exists) {
					dataFileWriter.appendTo(file);
				} else {
					dataFileWriter.create(gc.getSchema(), file);
				}
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
			T item = item = dataFileReader.next();
			return item;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> List<T> getAll(LoomMedia media, LoomMetaKey<T> metaKey) {
		DatumReader<T> datumReader = new SpecificDatumReader<>(metaKey.getValueClazz());
		File file = toMetaPath(media, metaKey).toFile();
		if (!file.exists()) {
			return Collections.emptyList();
		}
		try (DataFileReader<T> dataFileReader = new DataFileReader<>(file, datumReader)) {
			List<T> list = new ArrayList<T>();
			while (dataFileReader.hasNext()) {
				T item = dataFileReader.next();
				list.add(item);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		return toMetaPath(media, metaKey).toFile().exists();
	}

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
