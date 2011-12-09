package com.lm.keyrow;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lm.keyrow.filters.KeyRowFilter;

public class JVMKeyRowAccessor<T> implements KeyRowAccessor<T> {
	Map<CharSequence, Map<CharSequence, Object>> familyMap;
	
	public JVMKeyRowAccessor() {
		familyMap = new HashMap<CharSequence, Map<CharSequence, Object>>();
	}
	
	public JVMKeyRowAccessor(Map<CharSequence, Map<CharSequence, Object>> fm) {
		familyMap = fm;
	}
	
	private Map<CharSequence, T> getMap(CharSequence family) {
		Map<CharSequence, T> m = (Map<CharSequence, T>) familyMap.get(family);
		if (m == null) {
			m = new HashMap<CharSequence, T>();
			familyMap.put(family, (Map<CharSequence, Object>) m);
		}
		return m;
	}
	
	@Override
	public boolean update(CharSequence family, T row) throws KeyRowException {
		String key = KeyRowUtils.getKeyValue(row);
		
		T dest = getMap(family).get(key);
		if (dest == null)
			return false;
		
		Map<String, Object> m = KeyRowUtils.toMapIgnoreNull(row);
		KeyRowUtils.mapTo(m, dest);
		
		//KeyRowUtils.copy(row, dest);
		
		return true;
	}
	
	@Override
	public boolean remove(CharSequence family, CharSequence key) throws KeyRowException {
		if (getMap(family).containsKey(key) == false)
			return false;
		
		getMap(family).remove(key);
		
		return true;
	}
	
	@Override
	public List<T> query(CharSequence family, Condition<T> condition) throws KeyRowException {
		KeyRowFilter<T> filter = (KeyRowFilter<T>) condition.get(this, family);
		
		List<T> lst = new ArrayList<T>();
		Iterator<T> iter = (Iterator<T>) getMap(family).values().iterator();
		while (iter.hasNext()) {
			T crow = iter.next();
			
			int ret = filter.filter(crow);
			if (ret == KeyRowFilter.RETCODE_INCLUDE)
				lst.add(crow);
			else if (ret == KeyRowFilter.RETCODE_SKIP)
				continue;
			else if (ret == KeyRowFilter.RETCODE_NEXTROW)
				continue;
			
			if (filter.filterEnd())
				break;
		}
		
		return lst;
	}
	
	@Override
	public boolean insert(CharSequence family, T row) throws KeyRowException {
		if (getMap(family).containsKey(KeyRowUtils.getKeyValue(row)) == true)
			return false;
		
		T dupedObject = (T) KeyRowUtils.dup(row);
		String key = KeyRowUtils.getKeyValue(row);
		getMap(family).put(key, dupedObject);
		
		return true;
	}
	
	@Override
	public T get(CharSequence family, CharSequence key) throws KeyRowException {
		return getMap(family).get(key);
	}

	@Override
	public long size(CharSequence family) throws KeyRowException {
		return getMap(family).size();
	}
	
	@Override
	public long size(CharSequence family, Condition<T> condition) throws KeyRowException {
		if (condition == null)
			return getMap(family).size();
		
		KeyRowFilter<T> filter = (KeyRowFilter<T>) condition.get(this, family);
		
		long count = 0;
		Iterator<T> iter = (Iterator<T>) getMap(family).values().iterator();
		while (iter.hasNext()) {
			T crow = iter.next();

			int ret = filter.filter(crow);
			if (ret != KeyRowFilter.RETCODE_INCLUDE) {
				if (ret == KeyRowFilter.RETCODE_SKIP)
					continue;
				else if (ret == KeyRowFilter.RETCODE_NEXTROW)
					continue;
			}
			
			count++;
			
			if (filter.filterEnd())
				break;
		}
		
		return count;
	}
	
	@Override
	public long remove(CharSequence family, Condition<T> condition) throws KeyRowException {
		if (condition == null) {
			long size = getMap(family).size();
			getMap(family).clear();
			return size;
		}
		
		KeyRowFilter<T> filter = (KeyRowFilter<T>) condition.get(this, family);
		
		long count = 0;
		Iterator<T> iter = (Iterator<T>) getMap(family).values().iterator();
		while (iter.hasNext()) {
			T crow = iter.next();

			int ret = filter.filter(crow);
			if (ret == KeyRowFilter.RETCODE_INCLUDE)
				iter.remove();
			else if (ret == KeyRowFilter.RETCODE_SKIP)
				continue;
			else if (ret == KeyRowFilter.RETCODE_NEXTROW)
				continue;
			
			count++;
			
			if (filter.filterEnd())
				break;
		}
		
		return count;
	}
}
