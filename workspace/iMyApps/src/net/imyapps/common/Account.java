package net.imyapps.common;

import java.io.Serializable;

import com.lm.keyrow.KeyGetter;
import com.lm.keyrow.ValueGetter;
import com.lm.keyrow.ValueSetter;

public class Account implements Serializable {
	private static final long serialVersionUID = 4571677285031665955L;
	
	public static int STATUS_FROZEN = 0;
	public static int STATUS_CREATED = 1;
	public static int STATUS_NORMAL = 2;
	public static int STATUS_DELETED = 10;
	
	public static String ROLE_USER = "user";
	
	String uid;
	String loginName;
	String password;
	String question;
	String answer;
	String email;
	String fbid;
	Long createTime;
	Long updateTime;
	Long lastLoginTime;
	String role;
	Integer status;

	public static String getKey(String uid) {
		if (uid == null)
			throw new RuntimeException("Key(uid) cannot be null!");
		return uid;
	}
	
	@KeyGetter
	public String getKey() {
		return getKey(getUid());
	}
	
	@ValueGetter
	public String getLoginName() {
		return loginName;
	}
	@ValueSetter
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	@ValueGetter
	public String getPassword() {
		return password;
	}
	@ValueSetter
	public void setPassword(String password) {
		this.password = password;
	}
	@ValueGetter
	public String getQuestion() {
		return question;
	}
	@ValueSetter
	public void setQuestion(String question) {
		this.question = question;
	}
	@ValueGetter
	public String getAnswer() {
		return answer;
	}
	@ValueSetter
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	@ValueGetter
	public Long getCreateTime() {
		return createTime;
	}
	@ValueSetter
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	@ValueGetter
	public Long getUpdateTime() {
		return updateTime;
	}
	@ValueSetter
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	@ValueGetter
	public Integer getStatus() {
		return status;
	}
	@ValueSetter
	public void setStatus(Integer status) {
		this.status = status;
	}
	@ValueGetter
	public String getUid() {
		return uid;
	}
	@ValueSetter
	public void setUid(String uid) {
		this.uid = uid;
	}
	@ValueGetter
	public String getRole() {
		return role;
	}
	@ValueSetter
	public void setRole(String role) {
		this.role = role;
	}
	@ValueGetter
	public String getEmail() {
		return email;
	}
	@ValueSetter
	public void setEmail(String email) {
		this.email = email;
	}
	@ValueGetter
	public Long getLastLoginTime() {
		return lastLoginTime;
	}
	@ValueSetter
	public void setLastLoginTime(Long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	@ValueGetter
	public String getFbid() {
		return fbid;
	}
	@ValueSetter
	public void setFbid(String fbid) {
		this.fbid = fbid;
	}
}
