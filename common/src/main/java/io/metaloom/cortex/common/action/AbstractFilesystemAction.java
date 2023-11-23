package io.metaloom.cortex.common.action;

import static io.metaloom.cortex.api.action.ActionResult.CONTINUE_NEXT;
import static io.metaloom.utils.ConvertUtils.toHumanTime;
import static org.apache.commons.lang3.StringUtils.rightPad;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.ProcessorSettings;
import io.metaloom.cortex.api.option.action.ActionOptions;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public abstract class AbstractFilesystemAction implements FilesystemAction {

	private long current;
	private long total;

	private final LoomGRPCClient client;
	private final ActionOptions options;
	private final ProcessorSettings processorSettings;

	public AbstractFilesystemAction(LoomGRPCClient client, ProcessorSettings processorSettings, ActionOptions options) {
		this.client = client;
		this.options = options;
		this.processorSettings = processorSettings;
	}

	protected LoomGRPCClient client() {
		return client;
	}

	public boolean isOfflineMode() {
		return client() == null;
	}

	public ActionOptions options() {
		return options;
	}

	public ProcessorSettings processorSettings() {
		return processorSettings;
	}

	protected String shortHash(LoomMedia media) {
		return media.getSHA512().toString().substring(0, 8);
	}

	protected ActionResult completed(LoomMedia media, long start, String msg) {
		print(media, "COMPLETED", "(" + msg + ")", start);
		return ActionResult.processed(true, start);
	}

	protected ActionResult skipped(LoomMedia media, long start, String msg) {
		print(media, "SKIPPED", "(" + msg + ")", start);
		return ActionResult.skipped(true, start);
	}

	protected ActionResult failure(LoomMedia media, long start, String msg) {
		print(media, "FAILURE", "(" + msg + ")", start);
		return ActionResult.failed(true, start);
	}

	protected ActionResult done(LoomMedia media, long start, String info) {
		print(media, "DONE", "(" + info + ")", start);
		return ActionResult.processed(CONTINUE_NEXT, start);
	}

	@Override
	public void error(LoomMedia media, String msg) {
		String prefix = prefix(media);
		System.err.println(prefix + msg);
	}

	@Override
	public void print(LoomMedia file, String result, String msg, long start) {
		if (result == null) {
			result = "";
		}
		long dur = System.currentTimeMillis() - start;
		String prefix = prefix(file);
		result = rightPad(result, 12);
		String time = rightPad(" [" + toHumanTime(dur) + "] ", 12);
		System.out.println(prefix + result + time + msg);
	}

	private String prefix(LoomMedia media) {
		String progress = rightPad("[" + current + "/" + total + "] ", 17);
		if (media.getSHA512() == null) {
			return progress + rightPad("[" + name() + "]", 28);
		} else {
			return progress + rightPad(shortHash(media) + " [" + name() + "]", 28);
		}
	}

	@Override
	public void set(long current, long total) {
		this.current = current;
		this.total = total;
	}

	@Override
	public boolean isDryrun() {
		return processorSettings.isDryrun();
	}

}
