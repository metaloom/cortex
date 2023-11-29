package io.metaloom.cortex.common.action;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public interface ActionTestcases {

	@Test
	void testProcessing() throws IOException;

	@Test
	void testProcessVideo() throws IOException;

	@Test
	void testProcessDoc() throws IOException;

	@Test
	void testProcessImage() throws IOException;

	@Test
	void testProcessAudio() throws IOException;

	@Test
	void testProcessOther() throws IOException;

	@Test
	void testProcessMissing() throws IOException;

	@Test
	void testFailure() throws IOException;

	@Test
	void testDisabled() throws IOException;
}
