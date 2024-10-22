package io.metaloom.cortex.media.test;

import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.loom.test.data.TestMedia;

public abstract class AbstractBasicActionTest<T extends FilesystemAction<?>> extends AbstractActionTest<T> implements ActionTestcases {

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
		assertProcessedVideo(actionMock, media, video1);
	}

	protected void assertProcessedVideo(T actionMock, LoomMedia media, TestMedia video) throws IOException {
		assertProcessed(actionMock, media, video);
	}

	@Test
	@Override
	public void testProcessDoc() throws IOException {
		TestMedia testMedia = docDOCX();
		LoomMedia media = media(testMedia);
		T actionMock = action();
		assertProcessedDoc(actionMock, media, testMedia);
	}

	protected void assertProcessedDoc(T actionMock, LoomMedia media, TestMedia docMedia) throws IOException {
		assertProcessed(actionMock, media, docMedia);
	}

	@Test
	@Override
	public void testProcessImage() throws IOException {
		TestMedia image1 = image1();
		LoomMedia media = media(image1);
		T actionMock = action();
		assertProcessedImage(actionMock, media, image1);
	}

	protected void assertProcessedImage(T actionMock, LoomMedia media, TestMedia image) throws IOException {
		assertProcessed(actionMock, media, image);
	}

	@Test
	@Override
	public void testProcessAudio() throws IOException {
		TestMedia audio1 = audio1();
		LoomMedia media = media(audio1);
		T actionMock = action();
		assertProcessedAudio(actionMock, media, audio1);
	}

	protected void assertProcessedAudio(T actionMock, LoomMedia media, TestMedia audio) throws IOException {
		assertProcessed(actionMock, media, audio);
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
		assertDisabled(media, result);
	}

	protected void assertDisabled(LoomMedia media, ActionResult result) {
		assertThat(media).hasXAttr(0);
	}

	protected void assertSkipped(T actionMock, LoomMedia media) throws IOException {
		assertThat(media).hasXAttr(0);
	}

	private void assertProcessed(T actionMock, LoomMedia media, TestMedia testMedia) throws IOException {
		ActionResult result = actionMock.process(ctx(media));
		assertThat(result).isSuccess();
		assertThat(media).hasSHA512();
		assertProcessed(testMedia, media, result, actionMock);

		// Run the process again on the media to ensure that it will be skipped
		ActionResult result2 = actionMock.process(ctx(media));
		assertThat(result2).isSkipped();
	}

	protected abstract void disableAction(T actionMock);

}
