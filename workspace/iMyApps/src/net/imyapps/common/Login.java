package net.imyapps.common;

import java.io.Serializable;

public class Login implements Serializable {
	private static final long serialVersionUID = 5423399248693300183L;
	
	String loginName;
	String password;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
