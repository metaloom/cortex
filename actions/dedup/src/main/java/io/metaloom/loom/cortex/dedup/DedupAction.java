package io.metaloom.loom.cortex.dedup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.api.ActionResult;
import io.metaloom.cortex.action.api.ProcessableMedia;
import io.metaloom.cortex.action.common.AbstractFilesystemAction;
import io.metaloom.cortex.action.common.media.ProcessableMediaImpl;
import io.metaloom.cortex.action.common.settings.ProcessorSettings;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetRequest;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.fs.FileUtils;

public class DedupAction extends AbstractFilesystemAction<DedupActionSettings> {

	public static final Logger log = LoggerFactory.getLogger(DedupAction.class);

	public static final String NAME = "deduplicate";

	public DedupAction(LoomGRPCClient client, ProcessorSettings processorSettings, DedupActionSettings settings)
		throws FileNotFoundException {
		super(client, processorSettings, settings);

	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(ProcessableMedia media) throws IOException {
		long start = System.currentTimeMillis();
		String sha512 = media.getHash512();

		// Lets load the entry for the hash of the file
		AssetResponse asset = client().loadAsset(sha512).sync();

		// We found an item. Lets check it.
		if (asset != null) {
			if (asset.getPath() == null) {
				return done(media, start, "Source from db has no current path");
			}
			File dbFile = new File(asset.getPath());
			if (!dbFile.exists()) {
				return done(media, start, "Source from db not found for currentpath");
			}
			if (dbFile.equals(media.file())) {
				// All okay - its the same file
				return done(media, start, "same file");
			} else {
				AssetRequest newAsset = AssetRequest.newBuilder()
					.setSha512Sum(sha512)
					.setPath(media.absolutePath())
					.build();
				// client().storeAsset(sha512)
				ProcessableMedia foundMedia = new ProcessableMediaImpl(dbFile.toPath());
				if (!foundMedia.exists() || !media.exists()) {
					return done(media, start, "Source or dup not found");
				}

				// The hash matches the local file
				if (sha512.equalsIgnoreCase(foundMedia.getHash512())) {
					String pathA = media.absolutePath();
					String pathB = foundMedia.absolutePath();

					if (foundMedia.size() != dbFile.length()) {
						log.error("Inconsistent file found. The file sizes do not match. Please verify files manually! Halting processing now!");
						log.info("[A]: " + shortHash(media) + " E: " + media.exists() + " (" + dbFile.length() + " bytes) " + " => " + pathA);
						log.info("[B]: " + shortHash(foundMedia) + " E: " + foundMedia.exists() + " (" + foundMedia.size() + ") " + "  => " + pathB);
						System.in.read();
					}
					if (pathA.equalsIgnoreCase(pathB)) {
						return done(media, start, "same file");
					} else {
						if (log.isDebugEnabled()) {
							log.debug("[A]: " + shortHash(media) + " E: " + media.exists() + " => " + pathA);
							log.debug("[B]: " + shortHash(foundMedia) + " E: " + foundMedia.exists() + " => " + pathB);
						}
						// TODO configure target folder selection
						File targetFolder = new File("dups");
						if (!targetFolder.exists()) {
							if (!targetFolder.mkdirs()) {
								throw new RuntimeException("Could not create dups target dir {" + targetFolder.getAbsolutePath() + "}");
							}
						}
						moveMedia(media, targetFolder, start, "Dup Of: " + pathB);
						// We don't want to store the updated path for this media or alter the original file path.
						return ActionResult.processed(false, start);
					}
				} else {
					return done(media, start, "hash does not match");
				}
			}
		} else {
			return skipped(media, start, "no dup found");
		}

	}

	protected ActionResult moveMedia(ProcessableMedia media, File targetFolder, long start, String msg) {
		try {
			File targetFile = FileUtils.autoRotate(media.file(), targetFolder);
			print(media, "MOVING", "[" + media.path() + "] to [" + targetFolder.getAbsolutePath() + "] " + msg, start);
			if (!isDryrun()) {
				FileUtils.moveFile(media.file(), targetFile);
			}
			return ActionResult.processed(true, start);
		} catch (IOException e) {
			e.printStackTrace();
			print(media, "FAILED", "(Error while moving, " + msg + ")", start);
			return ActionResult.failed(true, start);
		}
	}


}
