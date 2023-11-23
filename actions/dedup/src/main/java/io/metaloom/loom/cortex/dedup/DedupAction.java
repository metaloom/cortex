package io.metaloom.loom.cortex.dedup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.common.media.impl.LoomMediaImpl;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.ProcessorSettings;
import io.metaloom.cortex.api.option.action.ActionOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetRequest;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.fs.FileUtils;

@Singleton
public class DedupAction extends AbstractFilesystemAction {

	public static final Logger log = LoggerFactory.getLogger(DedupAction.class);

	public static final String NAME = "deduplicate";

	@Inject
	public DedupAction(LoomGRPCClient client, ProcessorSettings processorSettings, ActionOptions options)
		throws FileNotFoundException {
		super(client, processorSettings, options);

	}

	@Override
	public String name() {
		return NAME;
	}

	@Override
	public ActionResult process(LoomMedia media) throws IOException {
		long start = System.currentTimeMillis();
		String sha512 = media.getSHA512();

		// Lets load the entry for the hash of the file
		AssetResponse asset = client().loadAsset(sha512).sync();

		// We found an item. Lets check it.
		if (asset != null) {
			if (asset.getFilename() == null) {
				return done(media, start, "Source from db has no current path");
			}
			File dbFile = new File(asset.getFilename());
			if (!dbFile.exists()) {
				return done(media, start, "Source from db not found for currentpath");
			}
			if (dbFile.equals(media.file())) {
				// All okay - its the same file
				return done(media, start, "same file");
			} else {
				AssetRequest newAsset = AssetRequest.newBuilder()
					.setSha512Sum(sha512)
					.setFilename(media.absolutePath())
					.build();
				// client().storeAsset(sha512)
				LoomMedia foundMedia = new LoomMediaImpl(dbFile.toPath());
				if (!foundMedia.exists() || !media.exists()) {
					return done(media, start, "Source or dup not found");
				}

				// The hash matches the local file
				if (sha512.equalsIgnoreCase(foundMedia.getSHA512())) {
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

	protected ActionResult moveMedia(LoomMedia media, File targetFolder, long start, String msg) {
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
