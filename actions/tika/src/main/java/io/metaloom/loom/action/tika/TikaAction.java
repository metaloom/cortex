package io.metaloom.loom.action.tika;

import static io.metaloom.worker.action.api.ProcessableMediaMeta.TIKA_FLAGS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.worker.action.api.ActionResult;
import io.metaloom.worker.action.api.ProcessableMedia;
import io.metaloom.worker.action.common.AbstractFilesystemAction;
import io.metaloom.worker.action.common.settings.ProcessorSettings;

public class TikaAction extends AbstractFilesystemAction<TikaActionSettings> {

	public static final Logger log = LoggerFactory.getLogger(TikaAction.class);

	public static String DONE_FLAG = "DONE";

	public static String NULL_FLAG = "NULL";

	private static final String NAME = "tika";

	public TikaAction(LoomGRPCClient client, ProcessorSettings processorSettings, TikaActionSettings settings) {
		super(client, processorSettings, settings);
	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(ProcessableMedia media) {
		long start = System.currentTimeMillis();
		String flags = getFlags(media);
		if (NULL_FLAG.equals(flags)) {
			return skipped(media, start, "(previously failed)");
		} else if (flags == null) {
			String info = "";
			String sha512 = media.getHash512();
			AssetResponse entry = client().loadAsset(sha512).sync();
			if (entry == null) {
				return parseMedia(start, media);
				// TODO utilize and store result
			} else {
				//TODO check whether db needs tika update
				return parseMedia(start, media);
				// TODO check response and assert whether processing is needed
				//return done(media, start, "from db");
				// writeFlags(media, DONE_FLAG);
			}
		} else {
			return done(media, start, "already processed");
		}
	}

	private ActionResult parseMedia(long start, ProcessableMedia media) {
		try {
			String result = MediaTikaParser.parse(media);
			writeFlags(media, DONE_FLAG);
			return done(media, start, "processed");
		} catch (Exception e) {
			log.error("Error while processing media " + media.path(), e);
			writeFlags(media, NULL_FLAG);
			return failure(media, start, "failed processing");
		}
	}

	private void writeFlags(ProcessableMedia media, String flags) {
		media.put(TIKA_FLAGS, flags);
	}

	private String getFlags(ProcessableMedia media) {
		String tikaFlags = media.get(TIKA_FLAGS);
		if (tikaFlags == null) {
			media.put(TIKA_FLAGS, tikaFlags);
		}
		return tikaFlags;
	}

}
