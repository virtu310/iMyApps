package net.imyapps.gwt.server.dao;

import net.imyapps.common.Account;


public interface AccountAdapter {
	public Account get(String uid) throws Exception;
	public Account getByLoginName(String loginName) throws Exception;
	public void put(Account account) throws Exception;
	public void update(Account account) throws Exception;
}
