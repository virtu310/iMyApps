package net.imyapps.gwt.server.dao;

import java.util.HashMap;
import java.util.Map;

import net.imyapps.common.PurchasedItem;

import org.apache.commons.beanutils.BeanUtils;

import com.lm.keyrow.KeyRowException;
import com.lm.keyrow.KeyRowUtils;

public class PurchasedItemLocalImpl implements PurchasedItemAdapter {
	Map<String, PurchasedItem> itemMap = 
									new HashMap<String, PurchasedItem>();
	
	public PurchasedItem get(String uid,
							 String platform, 
							 String buyerId, 
							 String itemId) throws Exception {
		PurchasedItem item = itemMap.get(
					PurchasedItem.getKey(uid, platform, buyerId, itemId));
		
		if (item != null) {
			PurchasedItem retItem = new PurchasedItem();
			BeanUtils.copyProperties(retItem, item);
			return retItem;
		}
		
		return null;
	}
	
	public void put(PurchasedItem item) throws KeyRowException {
		itemMap.put(KeyRowUtils.getKeyValue(item), item);
	}
}
