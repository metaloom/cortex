package io.metaloom.cortex.api.action.context.impl;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.ResultOrigin;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;

public class ActionContextImpl implements ActionContext {

	private final long start;
	private final LoomMedia media;
	private String info;
	private ResultOrigin origin;

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
	public ActionResult2 next() {
		return ActionResult2.processed(true);
	}

	@Override
	public ActionResult2 abort() {
		return ActionResult2.failed(false);
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
	public ActionContext skipped() {
		return this;
	}

}
