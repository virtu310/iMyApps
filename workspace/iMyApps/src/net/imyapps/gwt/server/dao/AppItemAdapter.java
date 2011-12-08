package net.imyapps.gwt.server.dao;

import net.imyapps.common.AppItem;

public interface AppItemAdapter {
		public long totalSize(String uid) throws Exception;
		public AppItem[] list(String uid, int begin, int size) throws Exception;
		public boolean add(String uid, AppItem appItem) throws Exception;
}
