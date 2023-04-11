package io.metaloom.worker.processor;

import java.io.IOException;
import java.nio.file.Path;

public interface MediaProcessor {

	void process(Path folder) throws IOException;

}
