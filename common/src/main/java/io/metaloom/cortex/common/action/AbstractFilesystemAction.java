package io.metaloom.cortex.common.action;

import static io.metaloom.cortex.api.action.ActionResult.CONTINUE_NEXT;
import static io.metaloom.utils.ConvertUtils.toHumanTime;
import static org.apache.commons.lang3.StringUtils.rightPad;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.action.ResultOrigin;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.api.option.action.CortexActionOptions;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public abstract class AbstractFilesystemAction<T extends CortexActionOptions> implements FilesystemAction {

	private ThreadLocal<Long> startTime = new ThreadLocal<>();

	private long current = 1L;
	private long total = 1L;

	private final LoomGRPCClient client;
	private final CortexOptions cortexOption;
	private final T option;

	public AbstractFilesystemAction(LoomGRPCClient client, CortexOptions cortexOption, T option) {
		this.client = client;
		this.cortexOption = cortexOption;
		this.option = option;
	}

	protected LoomGRPCClient client() {
		return client;
	}

	public boolean isOfflineMode() {
		return client() == null;
	}

	public T options() {
		return option;
	}

	public CortexOptions cortexOption() {
		return cortexOption;
	}

	public void markStart() {
		startTime.set(System.currentTimeMillis());
	}

	protected String shortHash(LoomMedia media) {
		return media.getSHA512().toString().substring(0, 8);
	}

	protected ActionResult completed(LoomMedia media, String msg) {
		print(media, "COMPLETED", "(" + msg + ")", startTime.get());
		return ActionResult.processed(true, startTime.get());
	}

	protected ActionResult skipped(LoomMedia media, String msg) {
		print(media, "SKIPPED", "(" + msg + ")", startTime.get());
		return ActionResult.skipped(true, startTime.get());
	}

	protected ActionResult failure(LoomMedia media, String msg) {
		print(media, "FAILURE", "(" + msg + ")", startTime.get());
		return ActionResult.failed(true, startTime.get());
	}

	protected ActionResult notFound(String msg) {
		print(null, "NOT_FOUND", "(" + msg + ")", startTime.get());
		return ActionResult.failed(true, startTime.get());
	}

	protected ActionResult success(LoomMedia media, String msg) {
		print(media, "DONE", "(" + msg + ")", startTime.get());
		return ActionResult.processed(CONTINUE_NEXT, startTime.get());
	}

	protected ActionResult success(LoomMedia media, ResultOrigin origin) {
		return success(media, origin.name());
	}

	@Override
	public void error(LoomMedia media, String msg) {
		String prefix = prefix(media);
		System.err.println(prefix + msg);
	}

	@Override
	public void print(LoomMedia media, String result, String msg, long start) {
		if (result == null) {
			result = "";
		}
		long dur = System.currentTimeMillis() - start;
		String prefix = prefix(media);
		result = rightPad(result, 12);
		String time = rightPad(" [" + toHumanTime(dur) + "] ", 12);
		System.out.println(prefix + result + time + msg);
	}

	private String prefix(LoomMedia media) {
		String progress = rightPad("[" + current + "/" + total + "] ", 17);
		if (media == null || media.getSHA512() == null) {
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
		return cortexOption().getProcessorSettings().isDryrun();
	}

}
