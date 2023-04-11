package io.metaloom.worker.action.media;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.metaloom.loom.client.grpc.LoomGRPCClient;
import io.metaloom.loom.client.grpc.method.LoomClientRequest;
import io.metaloom.loom.proto.AssetResponse;

public class LoomClientMock {

	@SuppressWarnings("unchecked")
	public static LoomGRPCClient mockGrpcClient() {
		LoomGRPCClient mock = mock(LoomGRPCClient.class);
		LoomClientRequest<AssetResponse> req = mock(LoomClientRequest.class);
		AssetResponse response = mock(AssetResponse.class);
		when(req.sync()).thenReturn(response);
		when(mock.loadAsset(anyString())).thenReturn(req);
		return mock;
	}
}
