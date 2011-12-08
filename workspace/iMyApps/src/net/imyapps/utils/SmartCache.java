package net.imyapps.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lm.cache.Cache;
import com.lm.cache.GAECache;
import com.lm.cache.JRECache;

public class SmartCache<T> implements Cache<T> {
	static Logger logger = LoggerFactory.getLogger(SmartCache.class);

	Cache<T> sysCache = new GAECache<T>();
	Cache<T> localCache = new JRECache<T>();
	
	public SmartCache() {
		
	}
	
	public SmartCache(String namespace) {
		setNamespace(namespace);
	}
	
	public void deleteLocalCache(CharSequence key) {
		localCache.delete(key);
	}
	
	@Override
	public T get(CharSequence key) {
		T value = localCache.get(key);
		if (value == null) {
			value = sysCache.get(key);
			if (value != null) {
				localCache.put(key, value);
				logger.info("get " + key + " from syscache!");
			}
		}
		else {
			logger.info("get " + key + " from localcache!");
		}
		
		return value;
	}
	
	@Override
	public void clearAll() {
		sysCache.clearAll();
		localCache.clearAll();
	}

	@Override
	public void setNamespace(CharSequence namespace) {
		sysCache.setNamespace(namespace);
		localCache.setNamespace(namespace);
	}

	@Override
	public CharSequence getNamespace() {
		return sysCache.getNamespace();
	}

	@Override
	public void put(CharSequence key, T value) {
		localCache.delete(key);
		sysCache.put(key, value);
	}
	
	@Override
	public void put(CharSequence key, T value, long expire_ms) {
		localCache.delete(key);
		sysCache.put(key, value, expire_ms);
	}

	@Override
	public boolean delete(CharSequence key) {
		localCache.delete(key);
		return sysCache.delete(key);
	}

}
