package io.metaloom.loom.cortex.action.tika;

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
import io.metaloom.utils.hash.SHA512;

@Singleton
public class TikaAction extends AbstractFilesystemAction {

	public static final Logger log = LoggerFactory.getLogger(TikaAction.class);

	public static String DONE_FLAG = "DONE";

	public static String NULL_FLAG = "NULL";

	private static final String NAME = "tika";

	@Inject
	public TikaAction(LoomGRPCClient client, CortexOptions cortexOption, TikaOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(LoomMedia media) {
		long start = System.currentTimeMillis();
		String flags = getFlags(media);
		if (NULL_FLAG.equals(flags)) {
			return skipped(media, start, "(previously failed)");
		} else if (flags == null) {
			String info = "";
			SHA512 sha512 = media.getSHA512();
			AssetResponse entry = client().loadAsset(sha512).sync();
			if (entry == null) {
				return parseMedia(start, media);
				// TODO utilize and store result
			} else {
				// TODO check whether db needs tika update
				return parseMedia(start, media);
				// TODO check response and assert whether processing is needed
				// return done(media, start, "from db");
				// writeFlags(media, DONE_FLAG);
			}
		} else {
			return done(media, start, "already processed");
		}
	}

	private ActionResult parseMedia(long start, LoomMedia media) {
		try {
			String result = MediaTikaParser.parse(media);
			// TODO store result
			media.setTikaFlags(DONE_FLAG);

			return done(media, start, "processed");
		} catch (Exception e) {
			log.error("Error while processing media " + media.path(), e);
			media.setTikaFlags(DONE_FLAG);
			return failure(media, start, "failed processing");
		}
	}

	private String getFlags(LoomMedia media) {
		String tikaFlags = media.getTikaFlags();
		if (tikaFlags == null) {
			media.setTikaFlags(tikaFlags);
		}
		return tikaFlags;
	}

}
