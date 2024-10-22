package io.metaloom.cortex.action.fp;

import static io.metaloom.cortex.api.media.LoomMedia.SHA_512_KEY;
import static io.metaloom.cortex.media.fingerprint.FingerprintMedia.FINGERPRINT_KEY;
import static io.metaloom.cortex.media.test.assertj.ActionAssertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import io.metaloom.cortex.api.action.ActionResult;
import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.option.CortexOptions;
import io.metaloom.cortex.media.test.AbstractBasicActionTest;
import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.test.data.TestMedia;

public class FingerprintActionTest extends AbstractBasicActionTest<FingerprintAction> {

	@Override
	protected void assertProcessed(TestMedia testMedia, LoomMedia media, ActionResult result, FingerprintAction actionMock) {
		assertThat(result).isSuccess();
		assertThat(media).hasXAttr(2).hasXAttr(SHA_512_KEY).hasXAttr(FINGERPRINT_KEY, data.sampleVideoFingerprint());
	}

	@Override
	protected void assertProcessedDoc(FingerprintAction actionMock, LoomMedia media, TestMedia doc) throws IOException {
		assertSkipped(actionMock, media);
	}

	@Override
	protected void assertProcessedAudio(FingerprintAction actionMock, LoomMedia media, TestMedia audio) throws IOException {
		assertSkipped(actionMock, media);
	}

	@Override
	protected void assertProcessedImage(FingerprintAction actionMock, LoomMedia media, TestMedia image) throws IOException {
		assertSkipped(actionMock, media);
	}

	@Override
	protected void disableAction(FingerprintAction actionMock) {
		FingerprintOptions options = actionMock.options();
		when(options.isEnabled()).thenReturn(false);
	}

	@Override
	public FingerprintAction mockAction(LoomClient client, CortexOptions cortexOptions) {
		FingerprintOptions options = mock(FingerprintOptions.class);
		when(options.isEnabled()).thenReturn(true);
		return new FingerprintAction(client, cortexOptions, options);
	}
}
