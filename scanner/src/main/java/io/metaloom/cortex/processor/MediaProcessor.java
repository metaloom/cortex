package io.metaloom.cortex.processor;

import java.io.IOException;
import java.nio.file.Path;

public interface MediaProcessor {

	void process(Path folder) throws IOException;

}
