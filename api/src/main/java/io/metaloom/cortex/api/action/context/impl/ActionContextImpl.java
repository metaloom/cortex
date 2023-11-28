package io.metaloom.cortex.api.action.context.impl;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.ResultOrigin;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;

public class ActionContextImpl implements ActionContext {

	private final long start;
	private final LoomMedia media;

	private String info;
	private ResultOrigin origin;
	private String skipReason;

	public ActionContextImpl(LoomMedia media) {
		this.media = media;
		this.start = System.currentTimeMillis();
	}

	@Override
	public long duration() {
		return System.currentTimeMillis() - start;
	}

	@Override
	public LoomMedia media() {
		return media;
	}

	@Override
	public ActionContext info(String info) {
		this.info = info;
		return this;
	}

	@Override
	public ActionResult next() {
		return ActionResult.processed(true);
	}

	@Override
	public ActionResult abort() {
		return ActionResult.failed(false);
	}

	@Override
	public ActionContext print(String string, String string2) {
		return this;
	}

	@Override
	public ActionContext origin(ResultOrigin origin) {
		this.origin = origin;
		return this;
	}

	@Override
	public ActionContext failure(String cause) {
		return this;
	}

	@Override
	public ActionContext skipped(String reason) {
		this.skipReason = reason;
		return this;
	}

}
