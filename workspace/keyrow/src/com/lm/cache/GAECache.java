package com.lm.cache;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class GAECache<T> implements Cache<T> {
	MemcacheService memcacheService;
	
	public GAECache() {
		memcacheService = MemcacheServiceFactory.getMemcacheService();
	}
	
	@Override
	public boolean delete(CharSequence key) {
		return memcacheService.delete(key);
	}

	@Override
	public T get(CharSequence key) {
		return (T) memcacheService.get(key);
	}

	@Override
	public CharSequence getNamespace() {
		return memcacheService.getNamespace();
	}

	@Override
	public void put(CharSequence key, T value) {
		memcacheService.put(key, value);
	}

	@Override
	public void put(CharSequence key, T value, long expireMs) {
		memcacheService.put(key, value, Expiration.byDeltaMillis((int) expireMs));
	}
	
	@Override
	public void setNamespace(CharSequence namespace) {
		memcacheService.setNamespace((String) namespace);
	}

	@Override
	public void clearAll() {
		memcacheService.clearAll();
	}

}
