package io.metaloom.cortex.action.hash;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.common.AbstractFilesystemAction;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.ProcessorSettings;
import io.metaloom.cortex.api.option.action.ActionOptions;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.hash.HashUtils;

@Singleton
public class MD5Action extends AbstractFilesystemAction {

	public static final Logger log = LoggerFactory.getLogger(MD5Action.class);

	private static final String NAME = "md5-hash";

	@Inject
	public MD5Action(LoomGRPCClient client, ProcessorSettings proocessorSettings, ActionOptions options) {
		super(client, proocessorSettings, options);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(LoomMedia media) {
		long start = System.currentTimeMillis();
		String md5 = getHashMD5(media);
		String info = "";
		if (md5 == null) {
			String sha512 = media.getSHA512();
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

	private void writeHashMD5(LoomMedia media, String hashSum) {
		media.setMD5(hashSum);
	}

	private String getHashMD5(LoomMedia media) {
		String md5sum = media.getMD5();
		if (md5sum == null) {
			md5sum = HashUtils.computeMD5(media.path());
			media.setMD5(md5sum);
		}
		return md5sum;
	}

}
