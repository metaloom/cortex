package io.metaloom.cortex.common.action;

import static io.metaloom.utils.ConvertUtils.toHumanTime;
import static org.apache.commons.lang3.StringUtils.rightPad;

import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.api.option.action.CortexActionOptions;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public abstract class AbstractFilesystemAction<T extends CortexActionOptions> implements FilesystemAction<T> {

	private long current = 1L;
	private long total = 1L;

	private final LoomGRPCClient client;
	private final CortexOptions cortexOption;
	private final T option;
	private final MetaStorage storage;

	public AbstractFilesystemAction(LoomGRPCClient client, CortexOptions cortexOption, T option, MetaStorage storage) {
		this.client = client;
		this.cortexOption = cortexOption;
		this.option = option;
		this.storage = storage;
	}

	@Override
	public void initialize() {
		// NOOP
	}

	@Override
	public MetaStorage storage() {
		return storage;
	}

	protected LoomGRPCClient client() {
		return client;
	}

	public boolean isOfflineMode() {
		return client() == null;
	}

	@Override
	public T options() {
		return option;
	}

	@Override
	public CortexOptions cortexOption() {
		return cortexOption;
	}

	protected String shortHash(LoomMedia media) {
		return media.getSHA512().toString().substring(0, 8);
	}

	// protected ActionResult2 completed(LoomMedia media, String msg) {
	// print(media, "COMPLETED", "(" + msg + ")");
	// return ActionResult2.processed(true, start());
	// }
	//
	// protected ActionResult2 skipped(LoomMedia media, String msg) {
	// print(media, "SKIPPED", "(" + msg + ")");
	// return ActionResult2.skipped(true, start());
	// }
	//
	// protected ActionResult2 failure(LoomMedia media, String msg) {
	// print(media, "FAILURE", "(" + msg + ")");
	// return ActionResult2.failed(true, start());
	// }
	//
	// protected ActionResult2 notFound(String msg) {
	// print(null, "NOT_FOUND", "(" + msg + ")");
	// return ActionResult2.failed(true, start());
	// }
	//
	// protected ActionResult2 success(LoomMedia media, String msg) {
	// print(media, "DONE", "(" + msg + ")");
	// return ActionResult2.processed(CONTINUE_NEXT, start());
	// }
	//
	// protected ActionResult2 success(LoomMedia media, ResultOrigin origin) {
	// return success(media, origin.name());
	// }
	//
	// public ActionResult2 skipped(boolean continueNext) {
	// return ActionResult2.skipped(continueNext, start());
	// }
	//
	@Override
	public void error(LoomMedia media, String msg) {
		String prefix = prefix(media);
		System.err.println(prefix + msg);
	}

	@Override
	public void print(ActionContext ctx, String result, String msg) {
		if (result == null) {
			result = "";
		}
		LoomMedia media = ctx.media();
		long dur = ctx.duration();
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
		return cortexOption().isDryrun();
	}

}
