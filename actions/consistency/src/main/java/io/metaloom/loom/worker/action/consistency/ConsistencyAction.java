package io.metaloom.loom.worker.action.consistency;

import static io.metaloom.worker.action.api.ActionResult.CONTINUE_NEXT;
import static io.metaloom.worker.action.api.ProcessableMediaMeta.ZERO_CHUNK_COUNT;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.partial.PartialFile;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;
import io.metaloom.worker.action.common.AbstractFilesystemAction;
import io.metaloom.worker.action.common.settings.ProcessorSettings;

public class ConsistencyAction extends AbstractFilesystemAction<ConsistencyActionSettings> {

	private static final String NAME = "consistency";

	public ConsistencyAction(LoomGRPCClient client, ProcessorSettings processorSettings, ConsistencyActionSettings settings) {
		super(client, processorSettings, settings);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(ProcessableMedia media) {
		long start = System.currentTimeMillis();
		if (!media.isVideo() && !media.isAudio()) {
			print(media, "SKIPPED", "(no video or audio)", start);
			return ActionResult.skipped(true, start);
		} else {
			try {
				String info = "";
				Long count = getZeroChunkCount(media);
				if (count == null) {
					String sha512 = media.getHash512();
					AssetResponse entry = client().loadAsset(sha512).sync();
					if (entry == null) {
						computeSum(media);
						info = "(computed)";
					} else {
						Long dbCount = entry.getZeroChunkCount();
						if (dbCount != null) {
							writeZeroChunkCount(media, dbCount);
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
	}

	private Long getZeroChunkCount(ProcessableMedia media) {
		return media.get(ZERO_CHUNK_COUNT);
	}

	private void writeZeroChunkCount(ProcessableMedia media, Long count) {
		media.put(ZERO_CHUNK_COUNT, count);
	}

	private void computeSum(ProcessableMedia media) throws NoSuchAlgorithmException, IOException {
		PartialFile pf = new PartialFile(media.path());
		long count = pf.computeZeroChunkCount();
		media.put(ZERO_CHUNK_COUNT, count);
	}
}
