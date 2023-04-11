package io.metaloom.worker.action.api;

import java.util.List;

import io.metaloom.loom.api.Loom;

public enum ProcessableMediaMeta {

	SHA_512("sha512", true, String.class),

	SHA_256("sha256", true, String.class),

	ZERO_CHUNK_COUNT("zero_chunk_count", true, Long.class),

	FACES("faces", false, List.class),

	FACE_CLUSTERS("face_clusters", false, null),

	CHUNK_HASH("chun_hash", true, String.class),

	THUMBNAIL_FLAGS("thumbnail_flags", true, String.class),

	TIKA_FLAGS("tika_flags", true, String.class),

	FINGERPRINT("fingerprint", true, String.class);

	private String name;

	private boolean persisted;

	private Class<?> type;

	private ProcessableMediaMeta(String name, boolean persisted, Class<?> type) {
		this.name = name;
		this.persisted = persisted;
		this.type = type;
	}

	public String key() {
		return fullKey(name);
	}

	public boolean isPersisted() {
		return persisted;
	}

	public Class<?> type() {
		return type;
	}

	public static String fullKey(String key) {
		return Loom.PREFIX + "_" + Loom.VERSION + "_" + key;
	}

	@Override
	public String toString() {
		return name + ":" + (persisted ? "persisted" : "not-persisted");
	}
}
