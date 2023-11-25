package io.metaloom.cortex.action.hash;

import io.metaloom.cortex.api.option.action.AbstractActionOptions;

public class HashOptions extends AbstractActionOptions<HashOptions> {

	private boolean md5 = true;

	private boolean sha512 = true;

	private boolean sha256 = true;

	private boolean chunkHash = true;

	@Override
	protected HashOptions self() {
		return this;
	}

	public boolean isMD5() {
		return md5;
	}

	public HashOptions setMD5(boolean flag) {
		this.md5 = flag;
		return this;
	}

	public boolean isChunkHash() {
		return chunkHash;
	}

	public HashOptions setChunkHash(boolean chunkHash) {
		this.chunkHash = chunkHash;
		return this;
	}

	public boolean isSHA256() {
		return sha256;
	}

	public HashOptions setSHA256(boolean flag) {
		this.sha256 = flag;
		return this;
	}

	public boolean isSHA512() {
		return sha512;
	}

	public HashOptions setSHA512(boolean flag) {
		this.sha512 = flag;
		return this;
	}

}
