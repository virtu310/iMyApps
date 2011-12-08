package net.imyapps.gwt.server.dao;

import java.util.List;

import com.lm.keyrow.EqualCondition;
import com.lm.keyrow.KR;

import net.imyapps.common.SessionRecord;
import net.imyapps.gwt.server.SessionManager;

public class SessionKeyRowImpl implements SessionAdapter {

	@Override
	public SessionRecord get(CharSequence sid) throws Exception {
		return (SessionRecord) KR.get(SessionRecord.class, sid);
	}

	@Override
	public boolean put(SessionRecord sr) throws Exception {
		return KR.put(sr);
	}

	@Override
	public boolean delete(CharSequence sid) throws Exception {
		return KR.remove(SessionRecord.class, sid);
	}

	@Override
	public void purge(String uid) throws Exception {
		SessionRecord con = new SessionRecord();
		con.setUid(uid);
		List<SessionRecord> lst = 
			KR.queryT(con, new EqualCondition<SessionRecord>(con));
		
		for (SessionRecord i : lst) {
			if (SessionManager.isSessionExpire(i.getCreateTime()))
				delete(i.getSid());
		}
	}
}
