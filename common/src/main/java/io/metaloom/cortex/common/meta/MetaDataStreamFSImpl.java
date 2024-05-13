package io.metaloom.cortex.common.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import io.metaloom.cortex.api.meta.MetaDataStream;
import io.metaloom.utils.fs.FileUtils;

public class MetaDataStreamFSImpl implements MetaDataStream {

	private final Path path;

	public MetaDataStreamFSImpl(Path path) {
		this.path = path;
	}

	@Override
	public OutputStream outputStream() throws IOException {
		FileUtils.ensureParentFolder(path);
		com.google.common.io.Files.touch(path.toFile());
		return Files.newOutputStream(path);
	}

	@Override
	public InputStream inputStream() throws IOException {
		if (!Files.exists(path)) {
			throw new IOException("Metadata file for " + path + " could not be found.");
		}
		return Files.newInputStream(path);
	}

}
