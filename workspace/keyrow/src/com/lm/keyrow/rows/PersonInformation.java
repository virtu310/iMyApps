package com.lm.keyrow.rows;

import com.lm.keyrow.KeyRow;

public class PersonInformation implements KeyRow {
	private static final long serialVersionUID = -8951671670598858023L;
	
	public static PersonInformation INSTANCE = new PersonInformation();
	
	public static int SEX_UNKNOW = 0;
	public static int SEX_MALE = 1;
	public static int SEX_FEMALE = 2;
	public static String STATUS_NORMAL = "normal";
	public static String STATUS_NEED_VERIFY = "need_verify";
	public static String STATUS_NEED_VERIFY_EMAIL = "need_verify_email";
	public static String STATUS_NEED_VERIFY_MOBILE = "need_verify_mobile";
	public static String STATUS_FROZEN = "frozen";
	
	String uid;
	String name;
	String birthday;
	Integer timezone;
	Integer sex;
	String smallpic;
	String bigpic;
	String status;
	String mobile;
	String email;
	String location;
	String url;
	Boolean single;
	Boolean mobile_verified;
	Boolean email_verified;
	Integer lucky_rate;
	Integer lead_times;
	Integer cancel_times;
	Integer absence_times;
	Integer attend_times;
	Integer good_times;
	Integer suggested_times;
	String current_signup;
	String modifiedTime;
	String createTime;
	
	public String key() {
		return getUid();
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getBigPic() {
		return bigpic;
	}
	public void setBigPic(String url) {
		this.bigpic = url;
	}
	public String getSmallPic() {
		return smallpic;
	}
	public void setSmallPic(String url) {
		this.smallpic = url;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getMobileVerified() {
		return this.mobile_verified;
	}
	public void setMobileVerified(Boolean verified) {
		this.mobile_verified = verified;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getSingle() {
		return single;
	}
	public void setSingle(Boolean single) {
		this.single = single;
	}
	public Integer getLeadTimes() {
		return lead_times;
	}
	public void setLeadTimes(Integer leadTimes) {
		lead_times = leadTimes;
	}
	public Integer getCancelTimes() {
		return cancel_times;
	}
	public void setCancelTimes(Integer cancelTimes) {
		cancel_times = cancelTimes;
	}
	public Integer getAbsenceTimes() {
		return absence_times;
	}
	public void setAbsenceTimes(Integer absenceTimes) {
		absence_times = absenceTimes;
	}
	public Integer getAttendTimes() {
		return attend_times;
	}
	public void setAttendTimes(Integer times) {
		attend_times = times;
	}
	public String getCurrentSignUp() {
		return current_signup;
	}
	public void setCurrentSignUp(String sid) {
		this.current_signup = sid;
	}
	public Integer getGoodTimes() {
		return good_times;
	}
	public void setGoodTimes(Integer times) {
		good_times = times;
	}
	public Integer getLuckyRate() {
		return lucky_rate;
	}
	public void setLuckyRate(Integer lr) {
		this.lucky_rate = lr;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public Integer getTimezone() {
		return timezone;
	}
	public void setTimezone(Integer timezone) {
		this.timezone = timezone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getEmailVerified() {
		return email_verified;
	}
	public void setEmailVerified(Boolean emailVerified) {
		email_verified = emailVerified;
	}
	public Integer getSuggestedTimes() {
		return suggested_times;
	}
	public void setSuggestedTimes(Integer suggestedTimes) {
		suggested_times = suggestedTimes;
	}
}
