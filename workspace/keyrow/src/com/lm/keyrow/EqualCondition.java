package com.lm.keyrow;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.lm.keyrow.filters.EqualFilter;

public class EqualCondition<T> implements Condition<T>, Sortable {
	public static int ASCENDING = 1;
	public static int DESCENDING = 2;
	
	T row;
	Map<String, Object> map;
	long offset;
	long size;
	String sort;
	Integer direction;
	
	public EqualCondition(T row) {
		this(row, 0, -1);
	}
	
	public EqualCondition(T row, long size) {
		this(row, 0, size);
	}
	
	public EqualCondition(T row, long offset, long size) {
		this.row = row;
		this.offset = offset;
		this.size = size;
	}
	
	public String getSort() {
		return this.sort;
	}
	
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public int getDirection() {
		return this.direction;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
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
			return new EqualFilter<T>(row, offset, size);
		else if (accessor instanceof DSKeyRowAccessor) {
			Query query = new Query((String) family);
			if (getMap() != null) {
				Iterator<String> iter = getMap().keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					query.addFilter(key, FilterOperator.EQUAL, getMap().get(key));
				}
			}
			
			if (StringUtils.isNotEmpty(this.sort)) {
				if (this.direction == null)
					query.addSort(sort);
				else if (this.direction == DESCENDING)
					query.addSort(sort, SortDirection.DESCENDING);
				else if (this.direction == DESCENDING)
					query.addSort(sort, SortDirection.ASCENDING);
				else
					query.addSort(sort);
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
