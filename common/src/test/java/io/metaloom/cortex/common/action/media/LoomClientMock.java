package io.metaloom.cortex.common.action.media;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.metaloom.loom.client.common.LoomClient;
import io.metaloom.loom.client.common.LoomClientException;
import io.metaloom.loom.client.common.LoomClientRequest;
import io.metaloom.loom.client.http.LoomHttpClient;
import io.metaloom.loom.rest.model.asset.AssetResponse;
import io.metaloom.utils.hash.SHA512;

public class LoomClientMock {

	// @SuppressWarnings("unchecked")
	// public static LoomGRPCClient mockGrpcClient() {
	// LoomGRPCClient mock = mock(LoomGRPCClient.class);
	// LoomClientRequest<AssetResponse> req = mock(LoomClientRequest.class);
	// AssetResponse response = mock(AssetResponse.class);
	// when(req.sync()).thenReturn(response);
	// when(mock.loadAsset(any())).thenReturn(req);
	// return mock;
	// }

	@SuppressWarnings("unchecked")
	public static LoomClient mockClient() throws LoomClientException {
		LoomHttpClient mock = mock(LoomHttpClient.class);
		LoomClientRequest<AssetResponse> req = mock(LoomClientRequest.class);
		AssetResponse response = mock(AssetResponse.class);
		when(req.sync()).thenReturn(response);
//		AssetId id = any();
		SHA512 sha512 = any();
		when(mock.loadAsset(sha512)).thenReturn(req);
		//when(mock.loadAsset(id)).thenReturn(req);
		return mock;
	}

}
