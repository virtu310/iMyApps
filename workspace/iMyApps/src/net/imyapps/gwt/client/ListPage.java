package net.imyapps.gwt.client;

import java.util.Date;

import net.imyapps.common.AppItem;
import net.imyapps.common.AppItems;

import org.restlet.client.data.MediaType;
import org.restlet.client.data.Preference;
import org.restlet.client.resource.Result;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ListPage {
	static ListPage instance;
	static int records_per_page = 20;
	
	int currPos = 0;
	Panel mainPanel = new VerticalPanel();

	public static void destroy() {
		if (instance != null && instance.mainPanel != null)
			instance.mainPanel.clear();
		
		instance = null;
	}
	
	public static void switchTo(Panels panels) {
		if (instance == null) {
			instance = new ListPage();
		}
		
		panels.getMain().clear();
		panels.getMain().add(instance.mainPanel);
	}

	public ListPage() {
		initPage2();
	}
	
	public void initPage() {
		mainPanel.clear();
		
		IMyApps.listAppResource.getClientResource().setReference(API.listApp());
		IMyApps.listAppResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
				Preference<MediaType>(MediaType.TEXT_HTML));

		final Anchor anList2 = new Anchor(IMyApps.msgs.bn_switchlist());
		anList2.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				currPos = 0;
				initPage2();
			}
		});
		
		class MyResult implements Result<String> {
			int trytimes, tried=0;
			public MyResult(int times) {
				trytimes = times;
			}
			public void onFailure(Throwable caught) {
				// Handle the error
				if (tried++ < trytimes)
					IMyApps.listAppResource.getHtml(this);
				else
					Window.alert(caught.getMessage());
			}
			public void onSuccess(String html) {
				mainPanel.clear();
				mainPanel.add(anList2);
				mainPanel.add(new HTML("<br />" + html));
			}
		};
		IMyApps.listAppResource.getHtml(new MyResult(3));
		
		mainPanel.add(anList2);
		mainPanel.add(new HTML(IMyApps.msgs.msg_loading()));
	}
	
	public void initPage2() {
		mainPanel.clear();
		final FlexTable flexTable = new FlexTable();
		flexTable.setStyleName("flexTable");
		
		String apiurl = API.appItem(currPos, records_per_page);
		
		IMyApps.appItemResource.getClientResource().setReference(apiurl);
		IMyApps.appItemResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
				Preference<MediaType>(MediaType.APPLICATION_JSON));
		
		final Result<AppItems> listObject = new Result<AppItems>() {

			public void onFailure(Throwable caught) {
				// Handle the error
				Window.alert(caught.getMessage());
			}
			
			public void onSuccess(AppItems appItems) {
				if (appItems == null || appItems.getAppItems() == null) {
					Window.alert("No records.");
					return;
				}
				
				AppItem items[] = appItems.getAppItems();
				
				flexTable.getColumnFormatter().addStyleName(0, "list-col1");
				flexTable.getColumnFormatter().addStyleName(1, "list-col2");
				flexTable.getColumnFormatter().addStyleName(2, "list-col3");
				flexTable.getColumnFormatter().addStyleName(3, "list-col4");
				
				for (int i=0; i<items.length; i++) {
					AppItem item = items[i];
					
					Image image = new Image(item.getSoftwareIcon57x57URL());
					HorizontalPanel pnIndex = new HorizontalPanel();
					Anchor anIndex = new Anchor(String.valueOf(currPos+1));
					pnIndex.add(anIndex);
					if (item.getGood() != null && item.getGood()) {
						pnIndex.add(new HTML("*"));
					}
					
					// index
					flexTable.getFlexCellFormatter().setColSpan(currPos, 0, 1);
					flexTable.setWidget(currPos, 0, pnIndex);
					
					// image
					flexTable.setWidget(currPos, 1, image);
					
					// description
					Panel pnDesc = new VerticalPanel();
					Anchor anName = new Anchor(item.getPlaylistName(), 
							"http://itunes.apple.com/tw/app/id" + item.getItemId(), "_blank");
					anName.addStyleName("desc-Name");
					pnDesc.add(anName);
					Label lbArtist = new Label(item.getPlaylistArtistName());
					lbArtist.addStyleName("desc-Artist");
					pnDesc.add(lbArtist);
					Label lbGenre = new Label(item.getGenre());
					lbGenre.addStyleName("desc-Genre");
					pnDesc.add(lbGenre);
					flexTable.setWidget(currPos, 2, pnDesc);
					
					// price & note
					Panel pnLast = new VerticalPanel();
					Label lbPrice;
					if (item.getBuyPrice() != null) {
						lbPrice = new Label(item.getBuyPrice());
					}
					else if (item.getPriceDisplay() != null) {
						lbPrice = new Label(item.getPriceDisplay());
					}
					else {
						lbPrice = new Label("None");
					}
					lbPrice.addStyleName("desc-Price");
					pnLast.add(lbPrice);
					Label lbNote = new Label();
					if (item.getNote() != null && item.getNote().length() > 0) {
						lbNote.setText(item.getNote());
						lbNote.addStyleName("desc-Note");
					}
					else {
						lbNote.setText(IMyApps.msgs.lb_modify());
						lbNote.addStyleName("desc-NoteModify");
					}
					lbNote.addClickHandler(new ModifyNoteHandler(lbNote, item));
					pnLast.add(lbNote);
					flexTable.setWidget(currPos, 3, pnLast);
					
					final DialogBox dialogBox = getDialogBox(item);
					anIndex.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.center();
						}
					});
					image.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.center();
						}
					});
					anIndex.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							dialogBox.center();
						}
					});
					
					if (currPos % 2 == 0)
						flexTable.getRowFormatter().
								setStyleName(currPos, "flexTable-OddRow");
					else
						flexTable.getRowFormatter().
								setStyleName(currPos, "flexTable-EvenRow");
					
					currPos++;
				}
				
				if (items.length > 0) {
					final Result listobj = this;
					Anchor more = new Anchor(IMyApps.msgs.lb_getmore());
					more.setStyleName("flexTable-LastRow");
					flexTable.setWidget(currPos, 0, more);
		            flexTable.getFlexCellFormatter().setColSpan(currPos, 0, 4);
					flexTable.getRowFormatter().
							setStyleName(currPos, "flexTable-LastRow");
					more.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							IMyApps.appItemResource.getClientResource().setReference(API.appItem(currPos, records_per_page));
							IMyApps.appItemResource.list(listobj);
							
							flexTable.setWidget(currPos, 0, new HTML(IMyApps.msgs.msg_loading()));
							flexTable.getFlexCellFormatter().setColSpan(currPos, 0, 4);
						}
					});
				}
				else if (currPos > 0) {
					flexTable.removeRow(flexTable.getRowCount()-1);
				}
				else {
					flexTable.setWidget(0, 0, 
							new HTML(IMyApps.msgs.msg_no_result()));
		            flexTable.getFlexCellFormatter().setColSpan(0, 0, 4);
					flexTable.getRowFormatter().setStyleName(0, "flexTable-LastRow");
				}
			}
		};
		
		//flexTable.setWidget(currPos, 0, new HTML(IMyApps.msgs.msg_loading()));
		IMyApps.appItemResource.list(listObject);
		
		flexTable.getRowFormatter().setStyleName(currPos, "flexTable-LastRow");
		flexTable.setWidget(currPos, 0, new HTML(IMyApps.msgs.msg_loading()));
		flexTable.getFlexCellFormatter().setColSpan(currPos, 0, 4);
		
		HorizontalPanel hp = new HorizontalPanel();
		//hp.add(anAdd);
		hp.add(new HTML("　　"));
		//mainPanel.add(hp);
		
		Anchor anList = new Anchor(IMyApps.msgs.bn_switchlist());
		anList.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				initPage();
			}
		});
		
		mainPanel.add(anList);		
		mainPanel.add(flexTable);
	}
	
	static DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy/MM/dd");
	static DateTimeFormat timeFormat = DateTimeFormat.getFormat("yyyy/MM/dd HH:mm");
	public static DialogBox getDialogBox(final AppItem item) {
		final DialogBox dialogBox = new DialogBox();
		
		final Label note;
		
		if (item.getNote() != null)
			note = new Label(item.getNote());
		else
			note = new HTML("<i>" + IMyApps.msgs.lb_modify() + "</i>");
		note.addClickHandler(new ModifyNoteHandler(note, item));
		
		final Label price;
		
		if (item.getBuyPrice() != null)
			price = new Label(item.getBuyPrice());
		else
			price = new HTML("<i>" + IMyApps.msgs.lb_modify() + "</i>");
		price.addClickHandler(new ModifyPriceHandler(price, item));

		Image image = new Image(item.getSoftwareIcon57x57URL());
		image.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
		Button bnClose = new Button(IMyApps.msgs.bn_close());
		bnClose.setStyleName("closeButton");
		bnClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
		final Anchor anGood;
		
		if (item.getGood() != null && item.getGood()) {
			anGood = new Anchor(IMyApps.msgs.bn_retrieve_good());
		}
		else {
			anGood = new Anchor(IMyApps.msgs.bn_good());
		}
		anGood.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				IMyApps.appItemResource.getClientResource().setReference(API.appItem());
				final Boolean origGood = item.getGood();
				if (item.getGood() != null && item.getGood()) {
					item.setGood(false);
				}
				else {
					item.setGood(true);
				}
				AppItems appItems = new AppItems();
				appItems.setAppItems(new AppItem[]{item});
				IMyApps.appItemResource.add(appItems, new Result<String[]>() {
					@Override
					public void onSuccess(String[] result) {
						if (item.getGood()) {
							anGood.setText(IMyApps.msgs.bn_retrieve_good());
						}
						else {
							anGood.setText(IMyApps.msgs.bn_good());
						}
					}
					@Override
					public void onFailure(Throwable caught) {
						item.setGood(origGood);
					}
				});
			}
		});
		
		FlexTable flexTable = new FlexTable();
		flexTable.setStyleName("desc-table");
		flexTable.setWidget(0, 0, image);
		flexTable.setWidget(0, 1, anGood);
		
		flexTable.setWidget(1, 0, new HTML(IMyApps.msgs.lb_itemName() + ": ")); 
		flexTable.setWidget(1, 1, new Anchor(item.getPlaylistName(), 
					"http://itunes.apple.com/tw/app/id" + item.getItemId(), "_blank"));
		
		flexTable.setWidget(2, 0, new HTML(IMyApps.msgs.lb_artistName() + ": "));
		flexTable.setWidget(2, 1, new HTML(item.getPlaylistArtistName()));
		
		flexTable.setWidget(3, 0, new HTML(IMyApps.msgs.lb_copyright() + ": "));
		flexTable.setWidget(3, 1, new HTML(item.getCopyright()));
		
		flexTable.setWidget(4, 0, new HTML(IMyApps.msgs.lb_genre() + ": "));
		flexTable.setWidget(4, 1, new HTML(item.getGenre()));
		
		flexTable.setWidget(5, 0, new HTML(IMyApps.msgs.lb_price() + ": "));
		if (item.getPriceDisplay() != null) {
			flexTable.setWidget(5, 1, new HTML(item.getPriceDisplay()));
		}
		else {
			flexTable.setWidget(5, 1, new HTML("None"));
		}
		
		flexTable.setWidget(6, 0, new HTML(IMyApps.msgs.lb_releaseDate() + ": "));
		flexTable.setWidget(6, 1, new HTML(dateFormat.format(new Date(item.getReleaseDate()))));
		
		flexTable.setWidget(7, 0, new HTML(IMyApps.msgs.lb_purchaseTime() + ": "));
		flexTable.setWidget(7, 1, new HTML(timeFormat.format(new Date(item.getPurchaseDate()))));
		
		flexTable.setWidget(8, 0, new HTML(IMyApps.msgs.lb_buyer() + ": "));
		flexTable.setWidget(8, 1, new HTML(item.getBuyerId()));
		
		flexTable.setWidget(9, 0, new HTML(IMyApps.msgs.lb_buyPrice() + ": "));
		flexTable.setWidget(9, 1, price);
		
		flexTable.setWidget(10, 0, new HTML(IMyApps.msgs.lb_note() + ": "));
		flexTable.setWidget(10, 1, note);
		
		flexTable.setWidget(11, 0, bnClose);
		flexTable.getRowFormatter().addStyleName(11, "flextable-LastRow");
		flexTable.getFlexCellFormatter().setColSpan(11, 0, 2);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(flexTable);
		
		dialogBox.add(vp);
		return dialogBox;
	}
	
	static class ModifyNoteHandler implements ClickHandler {
		Label note;
		AppItem item;
		
		ModifyNoteHandler(Label note, AppItem item) {
			this.note = note;
			this.item = item;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			final String input;
			if (item.getNote() != null) {
				input = Window.prompt(IMyApps.msgs.q_input_note(), item.getNote());
			}
			else {
				input = Window.prompt(IMyApps.msgs.q_input_note(), "");
			}
			if ((input != null && input.length() > 0) && 
				(item.getNote() == null || input.equals(item.getNote()) == false)) {
				final String origValue = item.getNote();
				IMyApps.appItemResource.getClientResource().setReference(API.appItem());
				item.setNote(input);
				AppItems appItems = new AppItems();
				appItems.setAppItems(new AppItem[]{item});
				IMyApps.appItemResource.add(appItems, new Result<String[]>() {
					@Override
					public void onSuccess(String[] result) {
						note.setText(input);
					}
					@Override
					public void onFailure(Throwable caught) {
						item.setNote(origValue);
					}
				});
			}
		}
	}
	
	static class ModifyPriceHandler implements ClickHandler {
		Label price;
		AppItem item;
		
		ModifyPriceHandler(Label price, AppItem item) {
			this.price = price;
			this.item = item;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			final String input;
			if (item.getBuyPrice() != null) {
				input = Window.prompt(IMyApps.msgs.q_input_price(), item.getBuyPrice());
			}
			else {
				input = Window.prompt(IMyApps.msgs.q_input_price(), "");
			}
			if ((input != null && input.length() > 0) && 
				(item.getBuyPrice() == null || input.equals(item.getBuyPrice()) == false)) {
				final String origValue = item.getBuyPrice();
				IMyApps.appItemResource.getClientResource().setReference(API.appItem());
				item.setBuyPrice(input);
				AppItems appItems = new AppItems();
				appItems.setAppItems(new AppItem[]{item});
				IMyApps.appItemResource.add(appItems, new Result<String[]>() {
					@Override
					public void onSuccess(String[] result) {
						price.setText(input);
					}
					@Override
					public void onFailure(Throwable caught) {
						item.setBuyPrice(origValue);
					}
				});
			}
		}
	}
}
