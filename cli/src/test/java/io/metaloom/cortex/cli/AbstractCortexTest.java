package io.metaloom.cortex.cli;

import java.nio.file.Paths;
import java.util.Map.Entry;

import io.metaloom.cortex.action.thumbnail.ThumbnailActionOptions;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.api.option.action.CortexActionOptions;

public abstract class AbstractCortexTest {

	protected CortexOptions createOptions() {
		CortexOptions options = new CortexOptions();
		options.setMetaPath(Paths.get("target", "meta"));
		options.getLoom().setHostname(null);

		// ActionOptions options = new ActionOptions();
		// options.getProcessorSettings().setPort(getPort());
		// options.getProcessorSettings().setHostname(getHostname());
		ThumbnailActionOptions thumbnailActionOptions = new ThumbnailActionOptions();
		thumbnailActionOptions.setThumbnailDir(Paths.get("target/loom-thumbnaildir"));
		options.getActions().put(ThumbnailActionOptions.KEY, thumbnailActionOptions);
		for (Entry<String, CortexActionOptions> entry : options.getActions().entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
		return options;
	}
}
