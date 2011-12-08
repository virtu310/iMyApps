package net.imyapps.common;

import java.io.Serializable;

import com.lm.keyrow.KeyGetter;
import com.lm.keyrow.ValueGetter;
import com.lm.keyrow.ValueSetter;

public class Item implements Serializable {
	private static final long serialVersionUID = 5388994037318899028L;
	
	String itemId;
	String itemName;
	String artistId;
	String artistName;
	String copyright;
	String kind;
	String playlistArtistName;
	String playlistName;
	String genre;
	String genreId;
	String price;
	String priceDisplay;
	String softwareIcon57x57URL;
	String vendorId;
	String softwareSupportedDeviceIds;
	String softwareVersionBundleId;
	String softwareVersionExternalIdentifiers;
	String versionRestrictions;
	Long releaseDate;
	String platform;
	Long createTime;
	Long updateTime;
	
	public static String getKey(String platform, String itemId) {
		if (platform == null || itemId == null)
			throw new RuntimeException("Key(platform,itemId) cannot be null!");

		StringBuilder sb = new StringBuilder('@');
		sb.append(platform).append('/').append(itemId);
		return sb.toString();
	}
	
	public void copyFrom(AppItem item) {
		setArtistName(item.getArtistName());
		setCopyright(item.getCopyright());
		setGenre(item.getGenre());
		setItemId(item.getItemId());
		setItemName(item.getItemName());
		setPlaylistArtistName(item.getPlaylistArtistName());
		setPlaylistName(item.getPlaylistName());
		setPrice(item.getPrice());
		setPriceDisplay(item.getPriceDisplay());
		setSoftwareIcon57x57URL(item.getSoftwareIcon57x57URL());
		setReleaseDate(item.getReleaseDate());
	}
	
	@KeyGetter
	public String getKey() {
		return getKey(getPlatform(), getItemId());
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
	public String getItemName() {
		return itemName;
	}
	@ValueSetter
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	@ValueGetter
	public String getArtistId() {
		return artistId;
	}
	@ValueSetter
	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}
	@ValueGetter
	public String getArtistName() {
		return artistName;
	}
	@ValueSetter
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	@ValueGetter
	public String getKind() {
		return kind;
	}
	@ValueSetter
	public void setKind(String kind) {
		this.kind = kind;
	}
	@ValueGetter
	public String getPlaylistArtistName() {
		return playlistArtistName;
	}
	@ValueSetter
	public void setPlaylistArtistName(String playlistArtistName) {
		this.playlistArtistName = playlistArtistName;
	}
	@ValueGetter
	public String getPlaylistName() {
		return playlistName;
	}
	@ValueSetter
	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}
	@ValueGetter
	public String getGenre() {
		return genre;
	}
	@ValueSetter
	public void setGenre(String genre) {
		this.genre = genre;
	}
	@ValueGetter
	public String getGenreId() {
		return genreId;
	}
	@ValueSetter
	public void setGenreId(String genreId) {
		this.genreId = genreId;
	}
	@ValueGetter
	public String getPrice() {
		return price;
	}
	@ValueSetter
	public void setPrice(String price) {
		this.price = price;
	}
	@ValueGetter
	public String getPriceDisplay() {
		return priceDisplay;
	}
	@ValueSetter
	public void setPriceDisplay(String priceDisplay) {
		this.priceDisplay = priceDisplay;
	}
	@ValueGetter
	public String getSoftwareIcon57x57URL() {
		return softwareIcon57x57URL;
	}
	@ValueSetter
	public void setSoftwareIcon57x57URL(String softwareIcon57x57URL) {
		this.softwareIcon57x57URL = softwareIcon57x57URL;
	}
	@ValueGetter
	public String getVendorId() {
		return vendorId;
	}
	@ValueSetter
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	@ValueGetter
	public String getSoftwareSupportedDeviceIds() {
		return softwareSupportedDeviceIds;
	}
	@ValueSetter
	public void setSoftwareSupportedDeviceIds(
									String softwareSupportedDeviceIds) {
		this.softwareSupportedDeviceIds = softwareSupportedDeviceIds;
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
	public String getVersionRestrictions() {
		return versionRestrictions;
	}
	@ValueSetter
	public void setVersionRestrictions(String versionRestrictions) {
		this.versionRestrictions = versionRestrictions;
	}
	@ValueGetter
	public String getSoftwareVersionBundleId() {
		return softwareVersionBundleId;
	}
	@ValueSetter
	public void setSoftwareVersionBundleId(String softwareVersionBundleId) {
		this.softwareVersionBundleId = softwareVersionBundleId;
	}
	@ValueGetter
	public Long getReleaseDate() {
		return releaseDate;
	}
	@ValueSetter
	public void setReleaseDate(Long releaseDate) {
		this.releaseDate = releaseDate;
	}
	@ValueGetter
	public String getSoftwareVersionExternalIdentifiers() {
		return softwareVersionExternalIdentifiers;
	}
	@ValueSetter
	public void setSoftwareVersionExternalIdentifiers(
			String softwareVersionExternalIdentifiers) {
		this.softwareVersionExternalIdentifiers = softwareVersionExternalIdentifiers;
	}
	@ValueGetter
	public String getCopyright() {
		return copyright;
	}
	@ValueSetter
	public void setCopyright(String copyright) {
		this.copyright = copyright;
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
}
