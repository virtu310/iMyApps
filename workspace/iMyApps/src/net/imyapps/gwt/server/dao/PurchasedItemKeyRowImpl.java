package net.imyapps.gwt.server.dao;

import net.imyapps.common.PurchasedItem;
import com.lm.keyrow.KR;
import com.lm.keyrow.KeyRowException;

public class PurchasedItemKeyRowImpl implements PurchasedItemAdapter {
	public PurchasedItem get(String uid,
							 String platform, 
							 String buyerId, 
							 String itemId) throws Exception {
		return (PurchasedItem) KR.get(PurchasedItem.class, PurchasedItem.getKey(uid, platform, buyerId, itemId));
	}
	
	public void put(PurchasedItem item) throws KeyRowException {
		KR.put(item);
	}
}
