package io.metaloom.cortex.api.media.type.handler;

import java.time.Duration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.metaloom.cortex.api.media.LoomMedia;
import io.metaloom.cortex.api.media.LoomMetaKey;
import io.metaloom.cortex.api.media.type.LoomMetaTypeHandler;

public abstract class AbstractCachingLoomTypeHandler implements LoomMetaTypeHandler {

	// TODO inject cache and make it configurable
	private Cache<String, Object> attrCache = Caffeine.newBuilder()
		.maximumSize(10_000)
		.expireAfterWrite(Duration.ofDays(30))
		.build();

	public Cache<String, Object> getCache() {
		return attrCache;
	}

	@Override
	public <T> boolean has(LoomMedia media, LoomMetaKey<T> metaKey) {
		// We check the cache and return a result if the value has been cached.
		String fullKey = metaKey.fullKey();
		T cacheValue = (T) attrCache.getIfPresent(fullKey);
		if (cacheValue != null) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T get(LoomMedia media, LoomMetaKey<T> metaKey) {
		String fullKey = metaKey.fullKey();
		T cacheValue = (T) attrCache.getIfPresent(fullKey);
		if (cacheValue != null) {
			return cacheValue;
		}
		return null;
	}

	@Override
	public <T> void put(LoomMedia media, LoomMetaKey<T> metaKey, T value) {
		String fullKey = metaKey.fullKey();
		attrCache.put(fullKey, value);
	}

}
