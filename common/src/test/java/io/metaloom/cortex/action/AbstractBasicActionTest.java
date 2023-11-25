package io.metaloom.cortex.action;

import static io.metaloom.loom.test.assertj.CortexAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.action.media.action.HashMedia;

public abstract class AbstractBasicActionTest<T extends FilesystemAction> extends AbstractActionTest<T> implements ActionTestcases {

	@Test
	@Override
	public void testProcessing() throws IOException {
		LoomMedia media = sampleVideoMedia();
		T actionMock = action();
		assertProcessed(actionMock, media);
	}

	protected abstract void assertProcessed(LoomMedia media, ActionResult result, T actionMock);

	@Test
	@Override
	public void testProcessVideo() throws IOException {
		LoomMedia media = sampleVideoMedia();
		T actionMock = action();
		assertProcessed(actionMock, media);

	}

	@Test
	@Override
	public void testProcessDoc() throws IOException {
		LoomMedia media = sampleDOCX();
		T actionMock = action();
		assertProcessed(actionMock, media);
	}

	@Test
	@Override
	public void testProcessImage() throws IOException {
		LoomMedia media = sampleImageMedia1();
		T actionMock = action();
		assertProcessed(actionMock, media);
	}

	@Test
	@Override
	public void testProcessAudio() throws IOException {
		LoomMedia media = sampleAudioMedia();
		T actionMock = action();
		assertProcessed(actionMock, media);
	}

	@Test
	@Override
	public void testProcessMissing() throws IOException {
		LoomMedia media = missingMP4();
		T actionMock = action();
		ActionResult result = actionMock.process(media);
		assertThat(result).isFailed();
	}

	@Test
	@Override
	public void testProcessOther() throws IOException {
		LoomMedia media = sampleBogusBin();
		T actionMock = action();
		ActionResult result = actionMock.process(media);
	}

	@Test
	@Override
	public void testFailure() throws IOException {
		LoomMedia media = sampleVideoMedia();
		T actionMock = action();
		ActionResult result = actionMock.process(media);
	}

	@Test
	@Override
	public void testDisabled() throws IOException {
		LoomMedia media = sampleVideoMedia();
		T actionMock = action();
		disableAction(actionMock);

		ActionResult result = actionMock.process(media);
		assertThat(result).isSkipped();
		assertThat(media).hasXAttr(1).hasXAttr(HashMedia.SHA_512_KEY);
	}

	private void assertProcessed(T actionMock, LoomMedia media) throws IOException {
		ActionResult result = actionMock.process(media);
		assertThat(result).isProcessed();
		assertThat(media).hasSHA512();
		assertProcessed(media, result, actionMock);

		ActionResult result2 = actionMock.process(media);
		assertThat(result2).isSkipped();
	}

	protected abstract void disableAction(T actionMock);

}
