package io.metaloom.cortex.api.media.type.handler.impl;

import java.io.IOException;

public class MetaStorageException extends RuntimeException {

	private static final long serialVersionUID = -6983367696239930209L;

	public MetaStorageException(String msg) {
		super(msg);
	}

	public MetaStorageException(String msg, IOException e) {
		super(msg, e);
	}

}
