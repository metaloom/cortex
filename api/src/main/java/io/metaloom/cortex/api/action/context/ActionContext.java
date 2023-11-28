package io.metaloom.cortex.api.action.context;

import java.util.concurrent.Callable;
import java.util.function.Function;

import io.metaloom.cortex.api.action.ActionResult;
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

	ActionContext skipped(String reason);

	ActionContext origin(ResultOrigin origin);

	ActionContext failure(String cause);

	ActionResult next();

	ActionResult abort();

	ActionContext print(String string, String string2);

	ActionContext info(String msg);

	default ActionResult flow(Function<LoomMedia, Boolean> processingFilter, Callable<LoomMedia> onFilesystem, Callable<LoomMedia> onLoom, Callable<LoomMedia> on) {
		return null;
	}

}
