package io.metaloom.loom.cortex.action.consistency;

import static io.metaloom.cortex.api.action.ActionResult.CONTINUE_NEXT;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.ProcessorSettings;
import io.metaloom.cortex.api.option.action.ActionOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.partial.PartialFile;

public class ConsistencyAction extends AbstractFilesystemAction {

	private static final String NAME = "consistency";

	public ConsistencyAction(LoomGRPCClient client, ProcessorSettings processorSettings, ActionOptions options) {
		super(client, processorSettings, options);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(LoomMedia media) {
		long start = System.currentTimeMillis();
		if (!media.isVideo() && !media.isAudio()) {
			print(media, "SKIPPED", "(no video or audio)", start);
			return ActionResult.skipped(true, start);
		}

		try {
			String info = "";
			Long count = media.getZeroChunkCount();
			if (count == null) {
				String sha512 = media.getSHA512();
				AssetResponse entry = null;
				if (!isOfflineMode()) {
					entry = client().loadAsset(sha512).sync();
				}
				if (entry == null) {
					computeSum(media);
					info = "(computed)";
				} else {
					Long dbCount = entry.getZeroChunkCount();
					if (dbCount != null) {
						media.setZeroChunkCount(dbCount);
						info = "(from db)";
					} else {
						computeSum(media);
						info = "(computed)";
					}
				}
			} else {
				info = "(from file)";
			}
			print(media, "DONE", info, start);
			return ActionResult.processed(CONTINUE_NEXT, start);
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			return ActionResult.failed(CONTINUE_NEXT, start);
		}

	}


	private void computeSum(LoomMedia media) throws NoSuchAlgorithmException, IOException {
		PartialFile pf = new PartialFile(media.path());
		long count = pf.computeZeroChunkCount();
		media.setZeroChunkCount(count);
	}
}
