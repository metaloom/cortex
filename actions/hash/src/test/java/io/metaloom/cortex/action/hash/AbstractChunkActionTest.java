package io.metaloom.cortex.action.hash;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

import io.metaloom.cortex.action.api.FilesystemAction;
import io.metaloom.cortex.action.api.ProcessableMedia;
import io.metaloom.cortex.action.media.AbstractWorkerTest;
import io.metaloom.cortex.action.media.LoomClientMock;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.Testdata;

public abstract class AbstractChunkActionTest<T extends FilesystemAction> extends AbstractWorkerTest {

	private Testdata data;

	private T action;

	@BeforeEach
	public void setup() throws IOException {
		action = mockAction();
		data = TestEnvHelper.prepareTestdata("action-test");
	}

	abstract T mockAction(LoomGRPCClient client);

	public T mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		return mockAction(client);
	}

	public T action() {
		return action;
	}

	public ProcessableMedia sampleVideoMedia() {
		return AbstractWorkerTest.sampleVideoMedia(data);
	}

	public String sampleVideoChunkHash() {
		return data.sampleVideoChunkHash();
	}

	public String sampleVideoSHA512() {
		return data.sampleVideoSHA512();
	}

	public String sampleVideoSHA256() {
		return data.sampleVideoSHA256();
	}

	public Testdata data() {
		return data;
	}

}
