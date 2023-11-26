package io.metaloom.cortex.api.action.context;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.ResultOrigin;
import io.metaloom.cortex.api.action.context.impl.ActionContextImpl;
import io.metaloom.cortex.api.action.media.LoomMedia;

public interface ActionContext {

	public static ActionContext create(LoomMedia media) {
		return new ActionContextImpl(media);
	}

	/**
	 * Returns the time in milliseconds that expired since the creation of the context.
	 * 
	 * @return
	 */
	long duration();

	LoomMedia media();

	ActionContext skipped();

	ActionContext origin(ResultOrigin origin);

	ActionContext failure(String cause);

	ActionResult2 next();

	ActionResult2 abort();

	ActionContext print(String string, String string2);

	ActionContext info(String msg);

}
