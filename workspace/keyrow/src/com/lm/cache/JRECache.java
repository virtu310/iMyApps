package com.lm.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class ItemInfo implements Serializable {
	private static final long serialVersionUID = 946922017193057072L;
	
	long createTime;
	long expireTime;
}

public class JRECache<T> implements Cache<T> {
	Map<CharSequence, T> map;
	Map<CharSequence, ItemInfo> mapTime;
	String nameSpace = "";
	int reachToPurgeSize = 3000;
	
	public JRECache() {
		map = new HashMap<CharSequence, T>();
		mapTime = new HashMap<CharSequence, ItemInfo>();
	}
	
	/**
	 * When size reach this size, it's will purge trash when next get value call. 
	 * @param size
	 */
	public void setPurgeSize(int size) {
		this.reachToPurgeSize = size;
	}

	/**
	 * Purge all timeout item from memory.
	 */
	public void purge() {
		ItemInfo ii;
		Iterator<CharSequence> iter = mapTime.keySet().iterator();
		long currTime = System.currentTimeMillis();
		
		while (iter.hasNext()) {
			CharSequence k = nameSpace + iter.next();
			ii = mapTime.get(k);
			if (ii == null) {
				mapTime.remove(k);
				continue;
			}
			if (currTime - ii.createTime > ii.expireTime) {
				map.remove(k);
				iter.remove();
			}
		}
	}

	@Override
	public boolean delete(CharSequence key) {
		mapTime.remove(nameSpace + key);
		return map.remove(nameSpace + key) != null;
	}

	@Override
	public T get(CharSequence key) {
		ItemInfo ii = mapTime.get(nameSpace + key);
		long currTime = System.currentTimeMillis();

		if (ii != null && ii.expireTime != -1) {
			if (currTime - ii.createTime > ii.expireTime) {
				delete(nameSpace + key);
				return null;
			}
		}
		
		if (map.size() > reachToPurgeSize) {
			purge();
		}
		
		return (T) map.get(nameSpace + key);
	}

	@Override
	public CharSequence getNamespace() {
		return nameSpace;
	}

	@Override
	public void put(CharSequence key, T value) {
		map.put(nameSpace + key, value);
		
		ItemInfo ii = new ItemInfo();
		ii.createTime = System.currentTimeMillis();
		ii.expireTime = -1;
		mapTime.put(nameSpace + key, ii);
	}

	@Override
	public void put(CharSequence key, T value, long expireMs) {
		map.put(nameSpace + key, value);
		
		ItemInfo ii = new ItemInfo();
		ii.createTime = System.currentTimeMillis();
		ii.expireTime = expireMs;
		mapTime.put(nameSpace + key, ii);
	}
	
	@Override
	public void setNamespace(CharSequence namespace) {
		nameSpace = (String) namespace;
	}

	@Override
	public void clearAll() {
		map.clear();
		mapTime.clear();
	}

}
