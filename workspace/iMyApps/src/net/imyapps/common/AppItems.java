package net.imyapps.common;

import java.io.Serializable;

public class AppItems implements Serializable {
	private static final long serialVersionUID = 963837761400876093L;
	
	AppItem appItems[];
	Long totalSize;
	Integer begin;
	Integer size;

	public AppItem[] getAppItems() {
		return appItems;
	}

	public void setAppItems(AppItem appItems[]) {
		this.appItems = appItems;
	}

	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}

	public Integer getBegin() {
		return begin;
	}

	public void setBegin(Integer begin) {
		this.begin = begin;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
}
