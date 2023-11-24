package io.metaloom.cortex.action.hash;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.SHA512;

@Singleton
public class ChunkHashAction extends AbstractFilesystemAction {

	public static final Logger log = LoggerFactory.getLogger(ChunkHashAction.class);

	private static final String NAME = "chunk-hash";

	@Inject
	public ChunkHashAction(LoomGRPCClient client, CortexOptions cortexOption, HashOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(LoomMedia media) {
		long start = System.currentTimeMillis();
		String info = "";
		String chunkHash = getChunkHash(media);
		if (chunkHash == null) {
			SHA512 sha512 = media.getSHA512();
			AssetResponse entry = client().loadAsset(sha512).sync();
			if (entry == null) {
				info = "hashed";
				getChunkHash(media);
			} else {
				String dbHash = entry.getChunkHash();
				if (dbHash != null) {
					media.setChunkHash(dbHash);
					info = "from db";
				}
			}
			return done(media, start, info);
		} else {
			return done(media, start, info);
		}
	}

	private String getChunkHash(LoomMedia media) {
		String chunkHashSum = media.getChunkHash();
		if (chunkHashSum == null) {
			chunkHashSum = HashUtils.computeChunkHash(media.file());
			media.setChunkHash(chunkHashSum);
		}
		return chunkHashSum;
	}

}
