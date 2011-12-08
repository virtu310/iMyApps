package net.imyapps.gwt.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.imyapps.common.AppItem;
import net.imyapps.common.CountAmount;
import net.imyapps.common.Statistic;
import net.imyapps.gwt.server.dao.AppItemAdapter;
import net.imyapps.utils.PriceUtils;

public class AppItemManager {
	private static AppItemAdapter adapter;
	
	public static AppItemAdapter setAdapter(AppItemAdapter newAdapter) {
		AppItemAdapter oldAdapter = adapter;
		adapter = newAdapter;
		return oldAdapter;
	}
	
	public static AppItemAdapter getAdapter() {
		if (adapter == null)
			throw new RuntimeException("AppItemAdapter must be setup!");
		
		return adapter;
	}
	
	public static long totalSize(String uid) {
		try {
			return getAdapter().totalSize(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static AppItem[] list(String uid, int begin, int size) {
		try {
			return getAdapter().list(uid, begin, size);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static AppItem[] search(String uid, String query) {
		try {
			AppItem items[] = getAdapter().list(uid, 0, -1);
			String keys[] = query.split(" ");
			ArrayList<AppItem> lst = new ArrayList<AppItem>();
			
			for (int i=0; i<keys.length; i++)
				keys[i] = keys[i].trim().toLowerCase();
			
			for (AppItem item : items) {
				boolean found = true;
				for (String k : keys) {
					if (k.endsWith("?") && k.endsWith("\\?") == false) {
						if (item.getPlaylistName().toLowerCase().startsWith(
								k.substring(0, k.length() - 1)) == false) {
							found = false;
							break;
						}
					}
					else {
						String ck;
						if (k.endsWith("\\?")) {
							ck = k.replaceAll("\\?", "?");
						}
						else {
							ck = k;
						}
						
						if ((item.getPlaylistName().toLowerCase().indexOf(ck) < 0) &&
							(StringUtils.isEmpty(item.getNote()) ||
								item.getNote().toLowerCase().indexOf(ck) < 0)) {
							found = false;
							break;
						}
					}
				}
				
				if (found == false) {
					continue;
				}
				
				lst.add(item);
				
				if (lst.size() > 50) {
					break;
				}
			}
			
			return lst.toArray(new AppItem[lst.size()]);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Statistic statistic(String uid, long begin, long end) {
		try {
			Statistic statistic;
			
			statistic = CacheManager.statisticCache.get(CacheManager.getCacheId(uid));
			if (statistic != null) {
				try {
					if (statistic.getPaid().getCount() >= 0)
						return statistic;
				}
				catch (Exception e) {
					CacheManager.statisticCache.delete(CacheManager.getCacheId(uid));
				}
			}
			
			statistic = new Statistic();
			
			AppItem items[] = getAdapter().list(uid, 0, -1);
			int amount = 0;
			int count = 0;
			int paidCount = 0;
			Map<String, CountAmount> byGenre = 
						new HashMap<String, CountAmount>();
			
			for (AppItem item : items) {
				if ((begin > 0 && item.getPurchaseDate() < begin) || 
						(end > 0 && item.getPurchaseDate() > end))
					continue;
				
				CountAmount ca = byGenre.get(item.getGenre());
				int price = -1;
				
				if (item.getBuyPrice() != null) {
					price = PriceUtils.to(item.getBuyPrice());
				}
				
				if (price == -1) {
					if (item.getPrice() != null) {
						price = PriceUtils.to(
											item.getPriceDisplay().charAt(0), 
											item.getPrice());
					}
					else {
						price = 0;
					}
				}
				
				if (ca == null) {
					ca = new CountAmount();
					ca.setAmount(price);
					ca.setCount(1);
					byGenre.put(item.getGenre(), ca);
				}
				else {
					ca.setAmount(ca.getAmount() + price);
					ca.setCount(ca.getCount() + 1);
				}
				
				amount += price;
				count++;
				
				if (price > 0) {
					paidCount++;
				}
			}
			
			CountAmount total = new CountAmount();
			total.setAmount(amount);
			total.setCount(count);
			CountAmount paid = new CountAmount();
			paid.setAmount(amount);
			paid.setCount(paidCount);
			statistic.setTotal(total);
			statistic.setPaid(paid);
			statistic.setByGenre(byGenre);
			
			CacheManager.statisticCache.put(
					CacheManager.getCacheId(uid), statistic, 1209600000);
			
			return statistic;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean add(String uid, AppItem appItem) {
		try {
			return getAdapter().add(uid, appItem);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
}
