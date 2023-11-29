package io.metaloom.cortex.action.hash;

import static io.metaloom.cortex.api.action.ResultOrigin.COMPUTED;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.meta.MetaStorage;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.common.action.AbstractFilesystemAction;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.utils.hash.HashUtils;
import io.metaloom.utils.hash.SHA512;

@Singleton
public class SHA512Action extends AbstractFilesystemAction<HashOptions> {

	public static final Logger log = LoggerFactory.getLogger(SHA512Action.class);

	@Inject
	public SHA512Action(LoomGRPCClient client, CortexOptions cortexOption, HashOptions options, MetaStorage storage) {
		super(client, cortexOption, options, storage);
	}

	@Override
	public String name() {
		return "SHA512";
	}

	@Override
	public ActionResult process(ActionContext ctx) {
		LoomMedia media = ctx.media();
		SHA512 hash = HashUtils.computeSHA512(media.file());
		media.setSHA512(hash);
		return ctx.origin(COMPUTED).next();
	}

}
