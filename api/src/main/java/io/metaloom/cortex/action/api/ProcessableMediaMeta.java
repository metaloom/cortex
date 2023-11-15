package io.metaloom.cortex.action.api;

import java.util.List;

import io.metaloom.cortex.LoomWorker;

public enum ProcessableMediaMeta {

	SHA_512("sha512", 1, true, String.class),

	SHA_256("sha256", 1, true, String.class),

	MD5("md5", 1, true, String.class),

	ZERO_CHUNK_COUNT("zero_chunk_count", 1, true, Long.class),

	FACES("faces", 1, false, List.class),

	CHUNK_HASH("chunk_hash", 1, true, String.class),

	THUMBNAIL_FLAGS("thumbnail_flags", 1, true, String.class),

	TIKA_FLAGS("tika_flags", 1, true, String.class),

	FINGERPRINT("fingerprint", 1, true, String.class);

	private String name;

	private int version;

	private boolean persisted;

	private Class<?> type;

	private ProcessableMediaMeta(String name, int version, boolean persisted, Class<?> type) {
		this.name = name;
		this.persisted = persisted;
		this.type = type;
		this.version = version;
	}

	public String key() {
		return fullKey(name, version);
	}

	public int version() {
		return version;
	}

	public boolean isPersisted() {
		return persisted;
	}

	public Class<?> type() {
		return type;
	}

	public static String fullKey(String key, int version) {
		return LoomWorker.PREFIX + "_" + LoomWorker.VERSION + "_" + key + "_v" + version;
	}

	@Override
	public String toString() {
		return name + ":" + (persisted ? "persisted" : "not-persisted");
	}
}
