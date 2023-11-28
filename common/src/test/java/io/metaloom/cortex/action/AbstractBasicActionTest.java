package io.metaloom.cortex.action;

import static io.metaloom.loom.test.assertj.CortexAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.cortex.api.action.media.action.HashMedia;
import io.metaloom.loom.test.data.TestMedia;

public abstract class AbstractBasicActionTest<T extends FilesystemAction> extends AbstractActionTest<T> implements ActionTestcases {

	@Test
	@Override
	public void testProcessing() throws IOException {
		TestMedia video1 = video1();
		LoomMedia media = media(video1);
		T actionMock = action();
		assertProcessed(actionMock, media, video1);
	}

	protected abstract void assertProcessed(TestMedia testMedia, LoomMedia media, ActionResult result, T actionMock);

	@Test
	@Override
	public void testProcessVideo() throws IOException {
		TestMedia video1 = video1();
		LoomMedia media = media(video1);
		T actionMock = action();
		assertProcessed(actionMock, media, video1);

	}

	@Test
	@Override
	public void testProcessDoc() throws IOException {
		TestMedia testMedia = docDOCX();
		LoomMedia media = media(testMedia);
		T actionMock = action();
		assertProcessed(actionMock, media, testMedia);
	}

	@Test
	@Override
	public void testProcessImage() throws IOException {
		TestMedia image1 = image1();
		LoomMedia media = media(image1);
		T actionMock = action();
		assertProcessed(actionMock, media, image1);
	}

	@Test
	@Override
	public void testProcessAudio() throws IOException {
		TestMedia audio1 = audio1();
		LoomMedia media = media(audio1);
		T actionMock = action();
		assertProcessed(actionMock, media, audio1);
	}

	@Test
	@Override
	public void testProcessMissing() throws IOException {
		LoomMedia media = mediaMissingMP4();
		T actionMock = action();
		ActionResult result = actionMock.process(ctx(media));
		assertThat(result).isFailed();
	}

	@Test
	@Override
	public void testProcessOther() throws IOException {
		LoomMedia media = mediaBogusBin();
		T actionMock = action();
		ActionResult result = actionMock.process(ctx(media));
	}


	@Test
	@Override
	public void testFailure() throws IOException {
		LoomMedia media = mediaVideo1();
		T actionMock = action();
		ActionResult result = actionMock.process(ctx(media));
	}

	@Test
	@Override
	public void testDisabled() throws IOException {
		LoomMedia media = mediaVideo1();
		T actionMock = action();
		disableAction(actionMock);

		ActionResult result = actionMock.process(ctx(media));
		assertThat(result).isSkipped();
		assertThat(media).hasXAttr(1).hasXAttr(HashMedia.SHA_512_KEY);
	}

	private void assertProcessed(T actionMock, LoomMedia media, TestMedia testMedia) throws IOException {
		ActionResult result = actionMock.process(ctx(media));
		assertThat(result).isProcessed();
		assertThat(media).hasSHA512();
		assertProcessed(testMedia, media, result, actionMock);

		ActionResult result2 = actionMock.process(ctx(media));
		assertThat(result2).isSkipped();
	}

	protected abstract void disableAction(T actionMock);

}
