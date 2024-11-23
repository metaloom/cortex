package io.metaloom.cortex.action.hash;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class HashActionOptions extends AbstractActionOptions<HashActionOptions> {

	public static final String KEY = "hash";

	private boolean md5 = true;

	private boolean sha512 = true;

	private boolean sha256 = true;

	private boolean chunkHash = true;

	@Override
	protected HashActionOptions self() {
		return this;
	}

	public boolean isMD5() {
		return md5;
	}

	public HashActionOptions setMD5(boolean flag) {
		this.md5 = flag;
		return this;
	}

	public boolean isChunkHash() {
		return chunkHash;
	}

	public HashActionOptions setChunkHash(boolean chunkHash) {
		this.chunkHash = chunkHash;
		return this;
	}

	public boolean isSHA256() {
		return sha256;
	}

	public HashActionOptions setSHA256(boolean flag) {
		this.sha256 = flag;
		return this;
	}

	public boolean isSHA512() {
		return sha512;
	}

	public HashActionOptions setSHA512(boolean flag) {
		this.sha512 = flag;
		return this;
	}

}
