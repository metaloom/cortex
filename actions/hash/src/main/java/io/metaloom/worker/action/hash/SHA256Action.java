package io.metaloom.worker.action.hash;

import static io.metaloom.worker.action.api.ProcessableMediaMeta.SHA_256;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;
import io.metaloom.worker.action.common.AbstractFilesystemAction;
import io.metaloom.worker.action.common.settings.ProcessorSettings;

public class SHA256Action extends AbstractFilesystemAction<HashActionSettings> {

	public static final Logger log = LoggerFactory.getLogger(SHA256Action.class);
	
	public static final String SHA256_ATTR_KEY = "sha256sum";

	private static final String NAME = "sha256-hash";

	public SHA256Action(LoomGRPCClient client, ProcessorSettings proocessorSettings, HashActionSettings settings) {
		super(client, proocessorSettings, settings);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(ProcessableMedia media) {
		long start = System.currentTimeMillis();
		String sha256 = getHash256(media);
		String info = "";
		if (sha256 == null) {
			String sha512 = media.getHash512();
			AssetResponse entry = client().loadAsset(sha512).sync();
			if (entry == null) {
				info = "hashed";
				getHash256(media);
			} else {
				String dbHash256 = entry.getSha256Sum();
				if (dbHash256 != null) {
					writeHash256(media, dbHash256);
					info = "from db";
				}
			}
			return done(media, start, info);
		} else {
			return done(media, start, info);
		}
	}

	private void writeHash256(ProcessableMedia media, String hashSum) {
		media.put(SHA_256, hashSum);
	}

	private String getHash256(ProcessableMedia media) {
		String hashSum256 = media.get(SHA_256);
		if (hashSum256 == null) {
			hashSum256 = HashUtils.computeSHA256(media.file());
			media.put(SHA_256, hashSum256);
		}
		return hashSum256;
	}

}
