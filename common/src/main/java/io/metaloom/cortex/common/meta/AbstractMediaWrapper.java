package io.metaloom.cortex.common.meta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.utils.hash.SHA512;

public abstract class AbstractMediaWrapper implements LoomMedia {

	private LoomMedia delegate;

	public AbstractMediaWrapper(LoomMedia media) {
		this.delegate = media;
	}

	@Override
	public boolean isVideo() {
		return delegate.isVideo();
	}

	@Override
	public boolean isImage() {
		return delegate.isImage();
	}

	@Override
	public boolean isAudio() {
		return delegate.isAudio();
	}

	@Override
	public boolean isDocument() {
		return delegate.isDocument();
	}

	@Override
	public File file() {
		return delegate.file();
	}

	@Override
	public Path path() {
		return delegate.path();
	}

	@Override
	public long size() throws IOException {
		return delegate.size();
	}

	@Override
	public String absolutePath() {
		return delegate.absolutePath();
	}

	@Override
	public boolean exists() {
		return delegate.exists();
	}

	@Override
	public InputStream open() throws FileNotFoundException {
		return delegate.open();
	}

	@Override
	public List<String> listXAttr() {
		return delegate.listXAttr();
	}

	@Override
	public MetaStorage storage() {
		return delegate.storage();
	}

	@Override
	public LoomMedia self() {
		return delegate.self();
	}

	@Override
	public SHA512 getSHA512() {
		return delegate.getSHA512();
	}

	@Override
	public void setSHA512(SHA512 hash) {
		delegate.setSHA512(hash);
	}

}
