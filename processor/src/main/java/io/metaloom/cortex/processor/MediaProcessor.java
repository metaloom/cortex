package io.metaloom.cortex.processor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface MediaProcessor {

	void process(List<String> enabledActions, Path folder) throws IOException;

}
