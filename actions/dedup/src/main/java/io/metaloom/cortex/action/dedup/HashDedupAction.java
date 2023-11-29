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

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractMediaAction;
import io.metaloom.cortex.common.media.impl.LoomMediaImpl;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.proto.AssetResponse;
import io.metaloom.utils.fs.FileUtils;
import io.metaloom.utils.hash.SHA512;

@Singleton
public class HashDedupAction extends AbstractMediaAction<DedupActionOptions> {

	public static final Logger log = LoggerFactory.getLogger(HashDedupAction.class);

	@Inject
	public HashDedupAction(LoomGRPCClient client, CortexOptions cortexOptions, DedupActionOptions options, MetaStorage storage) {
		super(client, cortexOptions, options, storage);
	}

	@Override
	public String name() {
		return "sha512-dedup";
	}

	@Override
	protected boolean isProcessable(ActionContext ctx) {
		return ctx.media().getSHA512() != null;
	}

	@Override
	protected boolean isProcessed(ActionContext ctx) {
		return false;
	}

	@Override
	protected ActionResult compute(ActionContext ctx, AssetResponse asset) throws IOException {
		if (isOfflineMode()) {
			return ctx.skipped("offline mode").next();
		} else {
			LoomMedia media = ctx.media();
			SHA512 sha512 = media.getSHA512();

			// We found an item. Lets check it.
			if (asset != null) {
				if (asset.getFilename() == null) {
					return ctx.info("Source from db has no current path").next();
				}
				File dbFile = new File(asset.getFilename());
				if (!dbFile.exists()) {
					return ctx.info("Source from db not found for currentpath").next();
				}
				if (dbFile.equals(media.file())) {
					// All okay - its the same file
					return ctx.info("same file").next();
				} else {
					// AssetRequest newAsset = AssetRequest.newBuilder()
					// .setSha512Sum(sha512.toString())
					// .setFilename(media.absolutePath())
					// .build();
					// client().storeAsset(sha512)
					LoomMedia foundMedia = new LoomMediaImpl(dbFile.toPath());
					if (!foundMedia.exists() || !media.exists()) {
						return ctx.info("Source or dup not found").next();
					}

					// The hash matches the local file
					if (sha512.equals(foundMedia.getSHA512())) {
						String pathA = media.absolutePath();
						String pathB = foundMedia.absolutePath();

						if (foundMedia.size() != dbFile.length()) {
							log.error("Inconsistent file found. The file sizes do not match. Please verify files manually! Halting processing now!");
							log.info("[A]: " + shortHash(media) + " E: " + media.exists() + " (" + dbFile.length() + " bytes) " + " => " + pathA);
							log.info(
								"[B]: " + shortHash(foundMedia) + " E: " + foundMedia.exists() + " (" + foundMedia.size() + ") " + "  => " + pathB);
							System.in.read();
						}
						if (pathA.equalsIgnoreCase(pathB)) {
							return ctx.info("same file").next();
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
							moveMedia(ctx, targetFolder, "Dup Of: " + pathB);
							// We don't want to store the updated path for this media or alter the original file path.
							return ctx.next();
						}
					} else {
						return ctx.next();
					}
				}
			} else {
				return ctx.skipped("no duplicate found").next();
			}
		}

	}

	protected ActionResult moveMedia(ActionContext ctx, Path targetFolder, String msg) {
		LoomMedia media = ctx.media();
		try {
			File targetFile = FileUtils.autoRotate(media.file(), targetFolder.toFile());
			print(ctx, "MOVING", "[" + media.path() + "] to [" + targetFolder.toAbsolutePath() + "] " + msg);
			if (!isDryrun()) {
				FileUtils.moveFile(media.file(), targetFile);
			}
			return ctx.next();
		} catch (IOException e) {
			e.printStackTrace();
			print(ctx, "FAILED", "(Error while moving, " + msg + ")");
			return ctx.failure("error while moving").next();
		}
	}

}
