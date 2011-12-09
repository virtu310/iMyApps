package com.lm.keyrow.rows;

import com.lm.keyrow.KeyRow;

public class Action implements KeyRow {
	private static final long serialVersionUID = -3902380060534512876L;
	
	public static Action INSTANCE = new Action();
	
	public static String STATUS_KICKOFF = "kickoff";
	public static String STATUS_CANCEL = "cancel";
	public static String STATUS_ACTION_FINISHED = "finished";
	public static String STATUS_OVERTIME = "overtime";
	public static String STATUS_CLOSE = "close";
	
	String aid;
	String status;
	String leader_uid;
	String action_time;
	String place;
	String description;
	String chat_messages;
	String result_messages;
	String cancel_uid;
	String createTime;
	
	public String key() {
		return getAid();
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLeaderUid() {
		return leader_uid;
	}
	public void setLeaderUid(String leaderUid) {
		leader_uid = leaderUid;
	}
	public String getActionTime() {
		return action_time;
	}
	public void setActionTime(String time) {
		this.action_time = time;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getChatMessages() {
		return chat_messages;
	}
	public void setChatMessages(String chatMessages) {
		chat_messages = chatMessages;
	}
	public String getResultMessages() {
		return result_messages;
	}
	public void setResultMessages(String resultMessages) {
		result_messages = resultMessages;
	}
	public String getCancelUid() {
		return cancel_uid;
	}
	public void setCancelUid(String cancelUid) {
		cancel_uid = cancelUid;
	}
}
