package net.imyapps.gwt.server.restlet;

import java.util.Date;
import net.imyapps.common.Account;
import net.imyapps.common.AppItem;
import net.imyapps.common.Statistic;
import net.imyapps.gwt.server.AccountManager;
import net.imyapps.gwt.server.AppItemManager;
import net.imyapps.gwt.server.Messages;
import net.imyapps.gwt.server.SessionManager;
import net.imyapps.utils.PriceUtils;

import org.apache.commons.lang.StringUtils;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.resource.Get;
import com.lm.utils.DateParser;

public class ListAppsResourceImpl extends ServerResource {
	
	@Override
	protected void doInit() throws ResourceException {
		try {
			super.doInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * for preload
	 */
	@Post
	public Representation post(Representation entity) throws ResourceException {
		try {
			Account account = SessionManager.getLoginUser(this);
			if (account == null) {
				Form form = new Form(entity);
				String id = form.getFirstValue("id");
				account = AccountManager.getAccountByUid(id);
			}
			
			if (account == null) {
				getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				return null;
			}
			AppItem[] appItems = AppItemManager.list(account.getUid(), 0, -1);
			Statistic statistic = AppItemManager.statistic(account.getUid(), 0, -1);
		}
		catch (Exception e) {
		}
		return null;
	}
	
	@Post
	public String postHtml(Representation entity) throws ResourceException {
		post(entity);
		return "ok";
	}
	
	@Get("txt")
	public String get2Text(Representation entity) throws ResourceException {
		Account account = SessionManager.getLoginUser(this);
		if (account == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}
		
		int begin = 0;
		int size = 20;
		
		try {
			begin = Integer.parseInt((getQuery().getFirstValue("begin")));
		}
		catch (Exception e) {
		}
		
		try {
			size = Integer.parseInt((getQuery().getFirstValue("size")));
		}
		catch (Exception e) {
		}

		StringBuilder sb = new StringBuilder();
		AppItem[] appItems = AppItemManager.list(account.getUid(), begin, size);
		for (AppItem item : appItems) {
			String displayPrice;
			
			if (StringUtils.isNotEmpty(item.getBuyPrice())) {
				displayPrice = item.getBuyPrice();
			}
			else if (StringUtils.isNotEmpty(item.getPriceDisplay())) {
				displayPrice = item.getPriceDisplay();
			}
			else {
				displayPrice = "None";
			}
			
			sb.append(item.getPlaylistName()).append(',').
			   append(item.getGenre()).append(',').
			   append(displayPrice).append(',').
			   append(item.getPurchaseDate()).append('\n');
		}
		
		return sb.toString();
	}
	
	@Get("html")
	public String get2Html(Representation entity) throws ResourceException {
		Account account = SessionManager.getLoginUser(this);
		if (account == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return null;
		}
		
		int begin = 0;
		int size = -1;
		
		try {
			begin = Integer.parseInt((getQuery().getFirstValue("begin")));
		}
		catch (Exception e) {
		}
		
		try {
			size = Integer.parseInt((getQuery().getFirstValue("size")));
		}
		catch (Exception e) {
		}
		
		StringBuilder sb = new StringBuilder();
		AppItem[] appItems = AppItemManager.list(account.getUid(), begin, size);
		int amount = 0;
		
		sb.append(Messages.msg.getString("lb_total") + ':').
				append(appItems.length).append("<br /><br /><table><tr><td width='15%'>").
				append(Messages.msg.getString("lb_purchaseTime")).
				append("</td><td width='60%'>").
				append(Messages.msg.getString("lb_itemName")).
				append("</td><td width='10%'>").
				append(Messages.msg.getString("lb_price")).
				append("</td><td width='15%'>").
				append(Messages.msg.getString("lb_note")).
				append("</td></tr>");
		
		for (AppItem item : appItems) {
			String displayPrice;
			
			if (StringUtils.isNotEmpty(item.getBuyPrice())) {
				displayPrice = item.getBuyPrice();
				amount += PriceUtils.to(item.getBuyPrice());
			}
			else if (StringUtils.isNotEmpty(item.getPriceDisplay()) &&
					 item.getPrice() != null) {
				displayPrice = item.getPriceDisplay();
				amount += PriceUtils.to(item.getPriceDisplay().charAt(0),
										item.getPrice());
			}
			else {
				displayPrice = "None";
			}
			
			sb.append("<tr><td>").
			   append(DateParser.dateFormat_date
					   .format(new Date(item.getPurchaseDate())));

			if (item.getGood() != null && item.getGood()) {
				sb.append("*");
			}
			
			sb.append("</td><td>").
			   append("<a href='http://itunes.apple.com/tw/app/id").
			   append(item.getItemId()).append("' target='_blank'>").
			   append(item.getPlaylistName()).
			   append("</a></td><td>").
			   append(displayPrice).
			   append("</td><td>").
			   append(item.getNote() == null ? "&nbsp;" : item.getNote()).
			   append("</td></tr>");
		}
		
		sb.append("<tr><td colspan='2'>&nbsp;</td><td>$").append((double)amount/1000).
		   append("</td><td>&nbsp;</td></tr>");
		
		sb.append("</table>");
		
		return sb.toString();
	}

}
