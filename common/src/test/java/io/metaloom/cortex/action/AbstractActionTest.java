package io.metaloom.cortex.action;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

import io.metaloom.cortex.action.media.AbstractMediaTest;
import io.metaloom.cortex.action.media.LoomClientMock;
import io.metaloom.cortex.api.action.FilesystemAction;
import io.metaloom.cortex.api.action.context.ActionContext;
import io.metaloom.cortex.api.action.media.LoomMedia;
import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.client.grpc.method.LoomClientRequest;
import io.metaloom.loom.proto.AssetResponse;

public abstract class AbstractActionTest<T extends FilesystemAction> extends AbstractMediaTest {

	private T action;

	@BeforeEach
	public void setup() throws IOException {
		System.out.println("----");
		action = mockAction();
	}

	public abstract T mockAction(LoomGRPCClient client);

	public T mockAction() {
		LoomGRPCClient client = LoomClientMock.mockGrpcClient();
		return mockAction(client);
	}

	public LoomGRPCClient mockClient(AssetResponse response) {
		LoomGRPCClient client = mock(LoomGRPCClient.class);
		LoomClientRequest<AssetResponse> request = mock(LoomClientRequest.class);
		when(request.sync()).thenReturn(response);

		when(client.loadAsset(any())).thenReturn(request);
		return client;
	}

	public T action() {
		return action;
	}
}
