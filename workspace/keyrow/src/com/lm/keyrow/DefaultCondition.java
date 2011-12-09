package com.lm.keyrow;

import org.apache.commons.lang.StringUtils;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.lm.keyrow.filters.DefaultFilter;

public class DefaultCondition<T> implements Condition<T>, Sortable {
	public static int ASCENDING = 1;
	public static int DESCENDING = 2;

	long offset;
	long size;
	String sort;
	Integer direction;

	public DefaultCondition(long offset, long size) {
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
	
	@Override
	public Object get(KeyRowAccessor<T> accessor, CharSequence family) throws KeyRowException {
		if (accessor instanceof JVMKeyRowAccessor)
			return new DefaultFilter<T>(offset, size);
		else if (accessor instanceof DSKeyRowAccessor) {
			Query query = new Query((String) family);
			
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
