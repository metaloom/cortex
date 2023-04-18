package io.metaloom.cortex.action.api;

import java.io.IOException;

public interface FilesystemAction {

	/**
	 * Name of the action.
	 * 
	 * @return
	 */
	String name();

	/**
	 * Process the media.
	 * 
	 * @param media
	 * @return
	 * @throws IOException 
	 */
	ActionResult process(ProcessableMedia media) throws IOException;

	void print(ProcessableMedia file, String result, String msg, long start);

	void error(ProcessableMedia media, String msg);

	default void flush() {
		// NOOP
	}

	void set(long current, long total);

	boolean isDryrun();

}
