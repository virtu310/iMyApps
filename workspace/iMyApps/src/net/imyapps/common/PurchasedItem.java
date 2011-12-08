package net.imyapps.common;

import java.io.Serializable;

import com.lm.keyrow.KeyGetter;
import com.lm.keyrow.ValueGetter;
import com.lm.keyrow.ValueSetter;

public class PurchasedItem implements Serializable {
	private static final long serialVersionUID = 1476568609904013251L;
	
	String uid;
	String itemId;
	String platform;
	String buyerId;
	String buyPrice;
	Long purchaseDate;
	String softwareVersionExternalIdentifier;
	String note;
	Boolean good;
	Long createTime;
	Long updateTime;

	public static String getKey(String uid,
								String platform, 
								String buyerId, 
								String itemId) {
		if (uid == null || platform == null || 
			buyerId == null || itemId == null)
			throw new RuntimeException("Key(uid,platform,buyerId,itemId)" +
									   " cannot be null!");

		StringBuilder sb = new StringBuilder(uid);
		
		sb.append('@').
		   append(buyerId).
		   append('.').
		   append(platform).
		   append('/').
		   append(itemId);
		
		return sb.toString();
	}
	
	public void copyFrom(AppItem item) {
		setBuyerId(item.getBuyerId());
		setBuyPrice(item.getBuyPrice());
		setNote(item.getNote());
		setPurchaseDate(item.getPurchaseDate());
	}
	
	@KeyGetter
	public String getKey() {
		return getKey(getUid(), getPlatform(), getBuyerId(), getItemId());
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
	public String getItemId() {
		return itemId;
	}
	@ValueSetter
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	@ValueGetter
	public String getPlatform() {
		return platform;
	}
	@ValueSetter
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	@ValueGetter
	public String getBuyerId() {
		return buyerId;
	}
	@ValueSetter
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	@ValueGetter
	public Long getPurchaseDate() {
		return purchaseDate;
	}
	@ValueSetter
	public void setPurchaseDate(Long purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	@ValueGetter
	public String getSoftwareVersionExternalIdentifier() {
		return softwareVersionExternalIdentifier;
	}
	@ValueSetter
	public void setSoftwareVersionExternalIdentifier(
			String softwareVersionExternalIdentifier) {
		this.softwareVersionExternalIdentifier = softwareVersionExternalIdentifier;
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
	public void setUpdateTime(Long modifyTime) {
		this.updateTime = modifyTime;
	}
	@ValueGetter
	public String getNote() {
		return note;
	}
	@ValueSetter
	public void setNote(String note) {
		this.note = note;
	}
	@ValueGetter
	public String getBuyPrice() {
		return buyPrice;
	}
	@ValueSetter
	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}
	@ValueGetter
	public Boolean getGood() {
		return good;
	}
	@ValueSetter
	public void setGood(Boolean good) {
		this.good = good;
	}

}
