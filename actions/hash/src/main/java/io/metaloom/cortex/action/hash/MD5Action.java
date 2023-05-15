package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.action.api.ProcessableMediaMeta.MD5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.ProcessableMedia;
import io.metaloom.cortex.action.common.AbstractFilesystemAction;
import io.metaloom.cortex.action.common.settings.ProcessorSettings;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.HashUtils;

public class MD5Action extends AbstractFilesystemAction<HashActionSettings> {

	public static final Logger log = LoggerFactory.getLogger(MD5Action.class);

	public static final String MD5SUM_ATTR_KEY = "md5sum";

	private static final String NAME = "md5-hash";

	public MD5Action(LoomGRPCClient client, ProcessorSettings proocessorSettings, HashActionSettings settings) {
		super(client, proocessorSettings, settings);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(ProcessableMedia media) {
		long start = System.currentTimeMillis();
		String md5 = getHashMD5(media);
		String info = "";
		if (md5 == null) {
			String sha512 = media.getHash512();
			AssetResponse asset = client().loadAsset(sha512).sync();
			if (asset == null) {
				info = "hashed";
				getHashMD5(media);
			} else {
				String dbMD5 = asset.getMd5Sum();
				if (dbMD5 != null) {
					writeHashMD5(media, dbMD5);
					info = "from db";
				}
			}
			return done(media, start, info);
		} else {
			return done(media, start, info);
		}
	}

	private void writeHashMD5(ProcessableMedia media, String hashSum) {
		media.put(MD5, hashSum);
	}

	private String getHashMD5(ProcessableMedia media) {
		String md5sum = media.get(MD5);
		if (md5sum == null) {
			md5sum = HashUtils.computeMD5(media.path());
			media.put(MD5, md5sum);
		}
		return md5sum;
	}

}
