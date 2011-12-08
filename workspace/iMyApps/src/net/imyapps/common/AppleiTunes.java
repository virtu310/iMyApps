package net.imyapps.common;

import java.io.Serializable;

public class AppleiTunes implements Serializable {
	private static final long serialVersionUID = -3389048128479318311L;
	
	String uid;
	String xml;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	
}
