package net.imyapps.gwt.server;

import java.io.Serializable;

import net.imyapps.common.Account;


public class SessionItem implements Serializable {
	private static final long serialVersionUID = 1052998688956968159L;
	
	String sid;
	Account account;
	Long createTime;
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
}
