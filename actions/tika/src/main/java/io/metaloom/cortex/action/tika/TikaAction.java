package io.metaloom.cortex.action.tika;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;

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
public class TikaAction extends AbstractFilesystemAction<TikaActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(TikaAction.class);

	public static String DONE_FLAG = "DONE";

	public static String NULL_FLAG = "NULL";

	@Inject
	public TikaAction(LoomGRPCClient client, CortexOptions cortexOption, TikaActionOptions options) {
		super(client, cortexOption, options);
	}

	@Override
	public String name() {
		return "tika";
	}

	@Override
	public ActionResult process(LoomMedia media) {
		markStart();
		String flags = getFlags(media);
		if (NULL_FLAG.equals(flags)) {
			return skipped(media, "(previously failed)");
		} else if (flags == null) {
			String info = "";
			SHA512 sha512 = media.getSHA512();
			AssetResponse entry = client().loadAsset(sha512).sync();
			if (entry == null) {
				return parseMedia(media);
				// TODO utilize and store result
			} else {
				// TODO check whether db needs tika update
				return parseMedia(media);
				// TODO check response and assert whether processing is needed
				// return done(media, start, "from db");
				// writeFlags(media, DONE_FLAG);
			}
		} else {
			return success(media, "already processed");
		}
	}

	private ActionResult parseMedia(LoomMedia media) {
		try {
			String result = MediaTikaParser.parse(media);
			// TODO store result
			media.setTikaFlags(DONE_FLAG);

			return success(media, COMPUTED);
		} catch (Exception e) {
			log.error("Error while processing media " + media.path(), e);
			media.setTikaFlags(DONE_FLAG);
			return failure(media, "failed processing");
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
