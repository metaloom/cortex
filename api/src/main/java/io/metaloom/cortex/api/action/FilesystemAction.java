package io.metaloom.cortex.api.action;

import java.io.IOException;

import io.metaloom.cortex.api.action.media.LoomMedia;

public interface FilesystemAction extends CortexAction {

	/**
	 * Process the media.
	 * 
	 * @param media
	 * @return
	 * @throws IOException
	 */
	ActionResult process(LoomMedia media) throws IOException;

	void print(LoomMedia file, String result, String msg, long start);

	void error(LoomMedia media, String msg);

	default void flush() {
		// NOOP
	}

	void set(long current, long total);


}
