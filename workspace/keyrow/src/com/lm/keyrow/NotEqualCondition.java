package com.lm.keyrow;

import java.util.Iterator;

import java.util.Map;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.lm.keyrow.filters.NotEqualFilter;

public class NotEqualCondition<T> implements Condition<T> {
	T row;
	Map<String, Object> map;
	long offset;
	long size;
	
	public NotEqualCondition(T row) {
		this(row, 0, -1);
	}
	
	public NotEqualCondition(T row, long size) {
		this(row, 0, size);
	}
	
	public NotEqualCondition(T row, long offset, long size) {
		this.row = row;
		this.offset = offset;
		this.size = size;
	}
	
	private Map<String, Object> getMap() {
		if (map == null) {
			try {
				map = KeyRowUtils.toMapIgnoreNull(row);
			} catch (KeyRowException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	@Override
	public Object get(KeyRowAccessor<T> accessor, CharSequence family) throws KeyRowException {
		if (accessor instanceof JVMKeyRowAccessor)
			return new NotEqualFilter<T>(row, offset, size);
		else if (accessor instanceof DSKeyRowAccessor) {
			Query query = new Query((String) family);
			if (getMap() != null) {
				Iterator<String> iter = getMap().keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					query.addFilter(key, FilterOperator.NOT_EQUAL, getMap().get(key));
				}
			}
			return query;
		}
		
		return null;
	}

	@Override
	public long getOffset() {
		return offset;
	}

	@Override
	public long getSize() {
		return size;
	}

}
