package io.metaloom.cortex.api.meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * DAO for IO stream access.
 */
public interface MetaDataStream {

	OutputStream outputStream() throws IOException;

	InputStream inputStream() throws IOException;
}
