package io.metaloom.cortex.action.hash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.media.LoomMedia;
import io.metaloom.cortex.action.common.AbstractFilesystemAction;
import io.metaloom.cortex.action.common.settings.ProcessorSettings;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.HashUtils;

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
	public ActionResult process(LoomMedia media) {
		long start = System.currentTimeMillis();
		String sha256 = media.getSHA256();
		String info = "";
		if (sha256 == null) {
			String sha512 = media.getSHA512();
			AssetResponse asset = client().loadAsset(sha512).sync();
			if (asset == null) {
				info = "hashed";
				getHash256(media);
			} else {
				String dbHash256 = asset.getSha256Sum();
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

	private void writeHash256(LoomMedia media, String hashSum) {
		media.setSHA256(hashSum);
	}

	private String getHash256(LoomMedia media) {
		String hashSum256 = media.getSHA256();
		if (hashSum256 == null) {
			hashSum256 = HashUtils.computeSHA256(media.file());
			media.setSHA256(hashSum256);
		}
		return hashSum256;
	}

}
