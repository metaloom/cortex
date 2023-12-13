package io.metaloom.cortex.common.media.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.common.media.AbstractFilesystemMedia;
import io.metaloom.utils.fs.FilterHelper;
import io.metaloom.utils.fs.XAttrUtils;
import io.metaloom.utils.hash.SHA512;

// The media is a singleton within the scope of the subcomponent
@Singleton
public class LoomMediaImpl extends AbstractFilesystemMedia {

	private final Path path;
	private final MetaStorage storage;

	@Inject
	public LoomMediaImpl(@Named("mediaPath") Path path, MetaStorage storage) {
		this.path = path;
		this.storage = storage;
	}

	@Override
	public Path path() {
		return path;
	}

	@Override
	public long size() throws IOException {
		return Files.size(path);
	}

	@Override
	public boolean isVideo() {
		return FilterHelper.isVideo(path());
	}

	@Override
	public boolean isDocument() {
		return FilterHelper.isDocument(path());
	}

	@Override
	public boolean isImage() {
		return FilterHelper.isImage(path());
	}

	@Override
	public boolean isAudio() {
		return FilterHelper.isAudio(path());
	}

	@Override
	public boolean exists() {
		return file().exists();
	}

	@Override
	public File file() {
		return path.toFile();
	}

	@Override
	public String absolutePath() {
		return file().getAbsolutePath();
	}

	@Override
	public InputStream open() throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file()));
	}

	@Override
	public List<String> listXAttr() {
		return XAttrUtils.listAttr(path());
	}

	@Override
	public LoomMedia self() {
		return this;
	}

	@Override
	public SHA512 getSHA512() {
		return get(SHA_512_KEY);
	}

	@Override
	public MetaStorage storage() {
		return storage;
	}

}
