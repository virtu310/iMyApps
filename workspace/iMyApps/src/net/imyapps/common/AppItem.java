package net.imyapps.common;

import java.io.Serializable;

public class AppItem implements Serializable {
	public static String PLATFORM_IOS = "iOS";
	
	private static final long serialVersionUID = 7668534575752346042L;
	
	// from Item
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
	String softwareIcon57x57URL;
	String vendorId;
	String softwareSupportedDeviceIds;
	String softwareVersionBundleId;
	String softwareVersionExternalIdentifiers;
	String versionRestrictions;
	Long releaseDate;
	String platform;
	String price;
	String priceDisplay;
	String softwareVersionExternalIdentifier;
	
	// from PurchasedItem
	String buyerId;
	String buyPrice;
	Long purchaseDate;
	String note;
	Boolean good;
	
	public void copyFrom(Item item) {
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
	
	public void copyFrom(PurchasedItem item) {
		setBuyerId(item.getBuyerId());
		setBuyPrice(item.getBuyPrice());
		setNote(item.getNote());
		setPurchaseDate(item.getPurchaseDate());
		setGood(item.getGood());
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getArtistId() {
		return artistId;
	}
	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getPlaylistArtistName() {
		return playlistArtistName;
	}
	public void setPlaylistArtistName(String playlistArtistName) {
		this.playlistArtistName = playlistArtistName;
	}
	public String getPlaylistName() {
		return playlistName;
	}
	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getGenreId() {
		return genreId;
	}
	public void setGenreId(String genreId) {
		this.genreId = genreId;
	}
	public String getSoftwareIcon57x57URL() {
		return softwareIcon57x57URL;
	}
	public void setSoftwareIcon57x57URL(String softwareIcon57x57URL) {
		this.softwareIcon57x57URL = softwareIcon57x57URL;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getSoftwareSupportedDeviceIds() {
		return softwareSupportedDeviceIds;
	}
	public void setSoftwareSupportedDeviceIds(String softwareSupportedDeviceIds) {
		this.softwareSupportedDeviceIds = softwareSupportedDeviceIds;
	}
	public String getSoftwareVersionBundleId() {
		return softwareVersionBundleId;
	}
	public void setSoftwareVersionBundleId(String softwareVersionBundleId) {
		this.softwareVersionBundleId = softwareVersionBundleId;
	}
	public String getSoftwareVersionExternalIdentifiers() {
		return softwareVersionExternalIdentifiers;
	}
	public void setSoftwareVersionExternalIdentifiers(
			String softwareVersionExternalIdentifiers) {
		this.softwareVersionExternalIdentifiers = softwareVersionExternalIdentifiers;
	}
	public String getVersionRestrictions() {
		return versionRestrictions;
	}
	public void setVersionRestrictions(String versionRestrictions) {
		this.versionRestrictions = versionRestrictions;
	}
	public Long getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Long releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPriceDisplay() {
		return priceDisplay;
	}
	public void setPriceDisplay(String priceDisplay) {
		this.priceDisplay = priceDisplay;
	}
	public Long getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Long purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getSoftwareVersionExternalIdentifier() {
		return softwareVersionExternalIdentifier;
	}
	public void setSoftwareVersionExternalIdentifier(
			String softwareVersionExternalIdentifier) {
		this.softwareVersionExternalIdentifier = softwareVersionExternalIdentifier;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}

	public Boolean getGood() {
		return good;
	}

	public void setGood(Boolean good) {
		this.good = good;
	}

}
