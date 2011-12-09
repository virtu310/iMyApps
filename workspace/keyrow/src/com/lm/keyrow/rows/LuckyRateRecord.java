package com.lm.keyrow.rows;

import com.lm.keyrow.KeyRow;

public class LuckyRateRecord implements KeyRow {
	private static final long serialVersionUID = -6413693078591058029L;

	public static int VALUE_BIRTHDAY = 3;
	public static int VALUE_SEX = 1;
	public static int VALUE_SINGLE = 1;
	public static int VALUE_EMAIL = 1;
	public static int VALUE_EMAIL_VERIFIED = 3;
	public static int VALUE_LOCATION = 2;
	public static int VALUE_MOBILE = 1;
	public static int VALUE_MOBILE_VERIFIED = 3;
	public static int MAX_VALUE_OF_FB_INVITE = 30;
	
	String uid;
	Boolean birthday = false;
	Boolean sex = false;
	Boolean single = false;
	Boolean location = false;
	Boolean mobile = false;
	Boolean mobile_verified = false;
	Boolean email_verified = false;
	Boolean email = false;
	Integer fb_invite = 0;
	
	public String key() {
		return uid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Boolean getBirthday() {
		return birthday;
	}
	public void setBirthday(Boolean birthday) {
		this.birthday = birthday;
	}
	public Boolean getSex() {
		return sex;
	}
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	public Boolean getSingle() {
		return single;
	}
	public void setSingle(Boolean single) {
		this.single = single;
	}
	public Boolean getLocation() {
		return location;
	}
	public void setLocation(Boolean location) {
		this.location = location;
	}
	public Boolean getMobile() {
		return mobile;
	}
	public void setMobile(Boolean mobile) {
		this.mobile = mobile;
	}
	public Integer getFBInvite() {
		return this.fb_invite;
	}
	public void setFBInvite(Integer count) {
		this.fb_invite = count;
	}
	public Boolean getEmail() {
		return email;
	}
	public void setEmail(Boolean email) {
		this.email = email;
	}
	public Boolean getMobileVerified() {
		return mobile_verified;
	}
	public void setMobileVerified(Boolean verifyMobile) {
		mobile_verified = verifyMobile;
	}
	public Boolean getEmailVerified() {
		return email_verified;
	}
	public void setEmailVerified(Boolean verifyEmail) {
		email_verified = verifyEmail;
	}
}
