package io.metaloom.worker.action.api;

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
	 */
	ActionResult process(ProcessableMedia media);

	void print(ProcessableMedia file, String result, String msg, long start);

	void error(ProcessableMedia media, String msg);

	default void flush() {
		// NOOP
	}

	void set(long current, long total);

}
