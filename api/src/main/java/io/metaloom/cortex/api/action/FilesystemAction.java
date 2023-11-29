package io.metaloom.cortex.api.action;

import java.io.IOException;

import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.action.CortexActionOptions;

/**
 * A Cortex action which is capable of processing {@link LoomMedia}.
 */
public interface FilesystemAction<T extends CortexActionOptions> extends CortexAction<T> {

	/**
	 * Process the media.
	 * 
	 * @param ctx
	 * @return
	 * @throws IOException
	 */
	ActionResult process(ActionContext ctx) throws IOException;

	default ActionResult process(LoomMedia media) throws IOException {
		return process(ActionContext.create(media));
	}

	void print(ActionContext ctx, String result, String msg);

	void error(LoomMedia media, String msg);

	default void flush() {
		// NOOP
	}

	void set(long current, long total);

}
