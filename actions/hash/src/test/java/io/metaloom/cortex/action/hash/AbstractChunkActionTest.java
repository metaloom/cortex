package io.metaloom.cortex.action.hash;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

import io.metaloom.cortex.action.api.FilesystemAction;
import io.metaloom.cortex.action.media.AbstractMediaTest;
import io.metaloom.cortex.action.media.LoomClientMock;
import io.metaloom.loom.client.grpc.LoomGRPCClient;

public abstract class AbstractChunkActionTest<T extends FilesystemAction> extends AbstractMediaTest {

	private T action;

	@BeforeEach
	public void setup() throws IOException {
		action = mockAction();
	}

	abstract T mockAction(LoomGRPCClient client);

	public T mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		return mockAction(client);
	}

	public T action() {
		return action;
	}

}
