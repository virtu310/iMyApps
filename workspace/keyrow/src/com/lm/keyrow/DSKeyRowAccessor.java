package com.lm.keyrow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class DSKeyRowAccessor<T> implements KeyRowAccessor<T> {
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	Class clazz;
	
	public DSKeyRowAccessor(Class clazz) {
		this.clazz = clazz;
	}
	
	public DSKeyRowAccessor(T t) {
		this(t.getClass());
	}
	
	@Override
	public boolean update(CharSequence family, T row) throws KeyRowException {
		try {
			String key = KeyRowUtils.getKeyValue(row);
			Entity entity = datastore.get(KeyFactory.createKey((String) family, (String) key));
			Map<String, Object> map = KeyRowUtils.toMapIgnoreNull(row);
			Iterator<String> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String k = iter.next();
				entity.setProperty(k, map.get(k));
			}
			datastore.put(entity);
			return true;
		}
		catch (EntityNotFoundException e) {
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new KeyRowException(e);
		}
	}
	
	@Override
	public boolean remove(CharSequence family, CharSequence key) throws KeyRowException {
		datastore.delete(KeyFactory.createKey((String) family, (String) key));
		return true;
	}
	
	@Override
	public List<T> query(CharSequence family, Condition<T> condition) throws KeyRowException {
		Query query;
		
		if (condition != null)
			query = (Query) condition.get(this, family);
		else
			query = new Query((String) family);

		long size;
		long offset;
		long index = 0;
		
		if (condition != null) {
			size = condition.getSize();
			offset = condition.getOffset();
		}
		else {
			size = -1;
			offset = 0;
		}
		
		List<T> rows = new ArrayList<T>();
		PreparedQuery pq = datastore.prepare(query);
		Iterator<Entity> iter = pq.asIterator();
		while (iter.hasNext()) {
			try {
				Entity entity = iter.next();
				
				if (offset != -1 && index++ < offset)
					continue;
				
				Map<String, Object> map = entity.getProperties();
				T row = (T) this.clazz.newInstance();
				KeyRowUtils.mapTo(map, row);
				rows.add(row);
				
				if (size != -1 && rows.size() == size)
					break;
			}
			catch (Exception e) {
				throw new KeyRowException(e);
			}
		}
		
		return rows;
	}
	
	@Override
	public boolean insert(CharSequence family, T row) throws KeyRowException {
		String key = KeyRowUtils.getKeyValue(row);
		Entity entity = new Entity((String) family, (String) key);
		try {
			datastore.get(entity.getKey());
			return false;
		}
		catch (EntityNotFoundException e) {
		}
		
		try {
			Map<String, Object> map = KeyRowUtils.toMap(row);
			Iterator<String> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String k = iter.next();
				entity.setProperty(k, map.get(k));
			}
			datastore.put(entity);
			return true;	
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new KeyRowException(e);
		}
	}
	
	@Override
	public T get(CharSequence family, CharSequence key) throws KeyRowException {
		try {
			Entity entity = datastore.get(KeyFactory.createKey((String) family, (String) key));
			Map<String, Object> map = entity.getProperties();
			T row = (T) this.clazz.newInstance();
			KeyRowUtils.mapTo(map, row);
			return row;
		} catch (EntityNotFoundException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new KeyRowException(e);
		}
		
	}

	@Override
	public long size(CharSequence family) throws KeyRowException {
		return size(family, null);
	}
	
	@Override
	public long size(CharSequence family, Condition<T> condition) throws KeyRowException {
		Query query;
		
		if (condition != null)
			query = (Query) condition.get(this, family);
		else
			query = new Query((String) family);
		
		query.setKeysOnly();
		
		PreparedQuery pq = datastore.prepare(query);
		return pq.countEntities();
	}
	
	@Override
	public long remove(CharSequence family, Condition<T> condition) throws KeyRowException {
		Query query;
		
		if (condition != null)
			query = (Query) condition.get(this, family);
		else
			query = new Query((String) family);
		
		query.setKeysOnly();
		long size;
		long offset;
		long index = 0;
		
		if (condition != null) {
			size = condition.getSize();
			offset = condition.getOffset();
		}
		else {
			size = -1;
			offset = 0;
		}
		
		List<Key> keys = new ArrayList<Key>();
		PreparedQuery pq = datastore.prepare(query);
		Iterator<Entity> iter = pq.asIterator();
		while (iter.hasNext()) {
			if (offset != -1 && index++ < offset)
				continue;
			
			Entity entity = iter.next();
			keys.add(entity.getKey());
			
			if (size != -1 && keys.size() == size)
				break;
		}
		datastore.delete(keys);
		
		return keys.size();
	}
}
