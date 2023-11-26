package io.metaloom.cortex.action.dedup;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.action.common.media.impl.LoomMediaImpl;
import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetRequest;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.fs.FileUtils;
import io.metaloom.utils.hash.SHA512;

@Singleton
public class HashDedupAction extends AbstractFilesystemAction<DedupActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(HashDedupAction.class);

	@Inject
	public HashDedupAction(LoomGRPCClient client, CortexOptions cortexOptions, DedupActionOptions options) {
		super(client, cortexOptions, options);
	}

	@Override
	public String name() {
		return "sha512-dedup";
	}

	@Override
	public ActionResult process(LoomMedia media) throws IOException {
		SHA512 sha512 = media.getSHA512();

		// Lets load the entry for the hash of the file
		AssetResponse asset = client().loadAsset(sha512).sync();

		// We found an item. Lets check it.
		if (asset != null) {
			if (asset.getFilename() == null) {
				return success(media, "Source from db has no current path");
			}
			File dbFile = new File(asset.getFilename());
			if (!dbFile.exists()) {
				return success(media, "Source from db not found for currentpath");
			}
			if (dbFile.equals(media.file())) {
				// All okay - its the same file
				return success(media, "same file");
			} else {
				AssetRequest newAsset = AssetRequest.newBuilder()
					.setSha512Sum(sha512.toString())
					.setFilename(media.absolutePath())
					.build();
				// client().storeAsset(sha512)
				LoomMedia foundMedia = new LoomMediaImpl(dbFile.toPath());
				if (!foundMedia.exists() || !media.exists()) {
					return success(media, "Source or dup not found");
				}

				// The hash matches the local file
				if (sha512.equals(foundMedia.getSHA512())) {
					String pathA = media.absolutePath();
					String pathB = foundMedia.absolutePath();

					if (foundMedia.size() != dbFile.length()) {
						log.error("Inconsistent file found. The file sizes do not match. Please verify files manually! Halting processing now!");
						log.info("[A]: " + shortHash(media) + " E: " + media.exists() + " (" + dbFile.length() + " bytes) " + " => " + pathA);
						log.info("[B]: " + shortHash(foundMedia) + " E: " + foundMedia.exists() + " (" + foundMedia.size() + ") " + "  => " + pathB);
						System.in.read();
					}
					if (pathA.equalsIgnoreCase(pathB)) {
						return success(media, "same file");
					} else {
						if (log.isDebugEnabled()) {
							log.debug("[A]: " + shortHash(media) + " E: " + media.exists() + " => " + pathA);
							log.debug("[B]: " + shortHash(foundMedia) + " E: " + foundMedia.exists() + " => " + pathB);
						}
						// TODO configure target folder selection
						Path targetFolder = options().getDupFolder();
						if (!Files.exists(targetFolder)) {
							try {
								Files.createDirectories(targetFolder);
							} catch (FileAlreadyExistsException e1) {
								// ignored
							} catch (Exception e2) {
								throw new RuntimeException("Could not create dups target dir {" + targetFolder.toAbsolutePath() + "}");
							}
						}
						moveMedia(media, targetFolder, "Dup Of: " + pathB);
						// We don't want to store the updated path for this media or alter the original file path.
						return success(media, "???");
					}
				} else {
					return success(media, "hash does not match");
				}
			}
		} else {
			return skipped(media, "no dup found");
		}

	}

	protected ActionResult moveMedia(LoomMedia media, Path targetFolder, String msg) {
		long start = 42L;
		try {
			File targetFile = FileUtils.autoRotate(media.file(), targetFolder.toFile());
			print(media, "MOVING", "[" + media.path() + "] to [" + targetFolder.toAbsolutePath() + "] " + msg, start);
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
