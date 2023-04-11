package io.metaloom.worker.action.hash;

import static io.metaloom.worker.action.api.ProcessableMediaMeta.CHUNK_HASH;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;
import io.metaloom.worker.action.common.AbstractFilesystemAction;
import io.metaloom.worker.action.common.settings.ProcessorSettings;

public class ChunkHashAction extends AbstractFilesystemAction<HashActionSettings> {

	public static final Logger log = LoggerFactory.getLogger(ChunkHashAction.class);

	private static final String NAME = "chunk-hash";

	public ChunkHashAction(LoomGRPCClient client, ProcessorSettings processorSettings, HashActionSettings settings) {
		super(client, processorSettings, settings);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(ProcessableMedia media) {
		long start = System.currentTimeMillis();
		String info = "";
		String chunkHash = getChunkHash(media);
		if (chunkHash == null) {
			String sha512 = media.getHash512();
			AssetResponse entry = client().loadAsset(sha512).sync();
			if (entry == null) {
				info = "hashed";
				getChunkHash(media);
			} else {
				String dbHash = entry.getChunkHash();
				if (dbHash != null) {
					writeChunkHash(media, dbHash);
					info = "from db";
				}
			}
			return done(media, start, info);
		} else {
			return done(media, start, info);
		}
	}

	private String getChunkHash(ProcessableMedia media) {
		String chunkHashSum = media.get(CHUNK_HASH);
		if (chunkHashSum == null) {
			chunkHashSum = HashUtils.computeChunkHash(media.file());
			writeChunkHash(media, chunkHashSum);
		}
		return chunkHashSum;
	}

	private void writeChunkHash(ProcessableMedia media, String hashSum) {
		media.put(CHUNK_HASH, hashSum);
	}

}
