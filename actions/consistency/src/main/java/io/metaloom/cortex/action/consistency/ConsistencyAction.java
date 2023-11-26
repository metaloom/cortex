package io.metaloom.cortex.action.consistency;

import static io.metaloom.cortex.api.action.ActionResult2.CONTINUE_NEXT;
import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.metaloom.cortex.api.action.ActionResult2;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.SHA512;
import io.metaloom.utils.hash.partial.PartialFile;

@Singleton
public class ConsistencyAction extends AbstractFilesystemAction<ConsistencyActionOptions> {

	@Inject
	public ConsistencyAction(LoomGRPCClient client, CortexOptions cortexOption, ConsistencyActionOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return "consistency";
	}

	@Override
	public ActionResult2 process(ActionContext ctx) {
		LoomMedia media = ctx.media();
		if (!media.isVideo() && !media.isAudio()) {
			ctx.print("SKIPPED", "(no video or audio)");
			return ctx.skipped().next();
		}

		try {
			String info = "";
			Long count = media.getZeroChunkCount();
			if (count == null) {
				SHA512 sha512 = media.getSHA512();
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
			ctx.print("DONE", info);
			return ctx.origin(COMPUTED).next();
		} catch (NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			return ctx.failure("").next();
		}

	}

	private void computeSum(LoomMedia media) throws NoSuchAlgorithmException, IOException {
		PartialFile pf = new PartialFile(media.path());
		long count = pf.computeZeroChunkCount();
		media.setZeroChunkCount(count);
	}

	
}
