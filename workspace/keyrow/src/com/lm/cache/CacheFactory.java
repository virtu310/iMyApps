package com.lm.cache;

public class CacheFactory {
	//static Cache cache = new GAECache<Object>();
	//static Cache cache = new JRECache<Object>();
	static String defaultCacheClass = "com.lm.cache.JRECache";
	
	public static Cache<Object> getCache() {
		return getCache(new Object(), Object.class.getName());
	}
	
	public static Cache<Object> getObjectCache(String namespace) {
		return getCache(new Object(), namespace);
	}
	
	public static <T> Cache<T> getCache(T row) {
		return getCache(row, row.getClass().getName());
	}
	
	public static <T> Cache<T> getCache(Class<T> clazz, T row) {
		return getCache(clazz, row, row.getClass().getName());
	}
	
	public static <T> Cache<T> getCache(T row, String namespace) {
		try {
			return getCache((Class<T>) Class.forName(defaultCacheClass), row, namespace);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static <T> Cache<T> getCache(Class<T> clazz, T row, String namespace) {
		try {
			Cache<T> c = (Cache<T>) clazz.newInstance();
			c.setNamespace(namespace);
			return c;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
