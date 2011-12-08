package net.imyapps.gwt.server.dao;

import net.imyapps.common.PurchasedItem;

public interface PurchasedItemAdapter {
	public PurchasedItem get(String uid,
							 String platform, 
							 String buyer, 
							 String itemId) throws Exception;
	
	public void put(PurchasedItem item) throws Exception;
}
