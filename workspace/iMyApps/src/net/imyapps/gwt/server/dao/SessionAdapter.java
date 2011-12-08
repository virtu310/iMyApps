package net.imyapps.gwt.server.dao;

import net.imyapps.common.SessionRecord;

public interface SessionAdapter {
	public SessionRecord get(CharSequence sid) throws Exception;
	public boolean put(SessionRecord sr)throws Exception;
	public boolean delete(CharSequence sid)throws Exception;
	public void purge(String uid) throws Exception;
}
