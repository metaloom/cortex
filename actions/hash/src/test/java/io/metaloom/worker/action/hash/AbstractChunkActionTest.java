package io.metaloom.worker.action.hash;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.test.TestEnvHelper;
import io.metaloom.loom.test.Testdata;
import io.metaloom.worker.action.api.FilesystemAction;
import io.metaloom.worker.action.api.ProcessableMedia;
import io.metaloom.worker.action.media.AbstractWorkerTest;
import io.metaloom.worker.action.media.LoomClientMock;

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
