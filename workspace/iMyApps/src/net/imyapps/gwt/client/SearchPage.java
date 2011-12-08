package net.imyapps.gwt.client;

import net.imyapps.common.AppItem;
import net.imyapps.common.AppItems;
import net.imyapps.gwt.client.ListPage.ModifyNoteHandler;

import org.restlet.client.resource.Result;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchPage {
	static SearchPage instance;
	
	Panel mainPanel;

	public static void destroy() {
		if (instance != null && instance.mainPanel != null)
			instance.mainPanel.clear();
		
		instance = null;
	}
	
	public static void switchTo(Panels panels) {
		if (instance == null) {
			instance = new SearchPage();
		}
		
		panels.getMain().clear();
		panels.getMain().add(instance.mainPanel);
	}

	public SearchPage() {
		initPage();
	}
	
	public void initPage() {
		mainPanel = new VerticalPanel();
		
		final FlexTable flexTable = new FlexTable();
		flexTable.setStyleName("flexTable");
		
		HorizontalPanel hpQuery = new HorizontalPanel();
		final TextBox tbQuery = new TextBox();
		Button bnQuery = new Button(IMyApps.msgs.bn_search());
		bnQuery.addStyleName("frameButton");
		hpQuery.add(tbQuery);
		hpQuery.add(bnQuery);
		
		final Result searchObject = new Result<AppItems>() {
			
			public void onFailure(Throwable caught) {
				// Handle the error
				Window.alert(caught.getMessage());
			}
			
			public void onSuccess(AppItems appItems) {
				flexTable.removeAllRows();
				
				flexTable.getColumnFormatter().addStyleName(0, "list-col1");
				flexTable.getColumnFormatter().addStyleName(1, "list-col2");
				flexTable.getColumnFormatter().addStyleName(2, "list-col3");
				flexTable.getColumnFormatter().addStyleName(3, "list-col4");
				
				AppItem items[] = appItems.getAppItems();
				
				for (int i=0; i<items.length; i++) {
					AppItem item = items[i];
					
					// index
					HorizontalPanel pnIndex = new HorizontalPanel();
					Anchor anIndex = new Anchor(String.valueOf(i+1));
					pnIndex.add(anIndex);
					if (item.getGood() != null && item.getGood()) {
						pnIndex.add(new HTML("*"));
					}
					flexTable.setWidget(i, 0, pnIndex);
					
					// image
					Image image = new Image(item.getSoftwareIcon57x57URL());
					flexTable.getFlexCellFormatter().setColSpan(i, 0, 1);
					flexTable.setWidget(i, 1, image);
					
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
					flexTable.setWidget(i, 2, pnDesc);

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
					flexTable.setWidget(i, 3, pnLast);
					
					final DialogBox dialogBox = ListPage.getDialogBox(item);
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
					
					if (i % 2 == 0)
						flexTable.getRowFormatter().
								setStyleName(i, "flexTable-OddRow");
					else
						flexTable.getRowFormatter().
								setStyleName(i, "flexTable-EvenRow");
				}
				
				if (flexTable.getRowCount() == 0) {
					flexTable.setWidget(0, 0, 
							new HTML(IMyApps.msgs.msg_no_found(tbQuery.getText())));
		            flexTable.getFlexCellFormatter().setColSpan(0, 0, 4);
					flexTable.getRowFormatter().
									setStyleName(0, "flexTable-LastRow");
				}
			}
		};
		
		class SearchHandler implements ClickHandler, KeyUpHandler {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					doSearch();
				}
			}

			@Override
			public void onClick(ClickEvent event) {
				doSearch();
			}
			
			public void doSearch() {
				flexTable.removeAllRows();
				
				if (tbQuery.getText().length() == 0) {
					return;
				}
				
				flexTable.setWidget(0, 0, new HTML(IMyApps.msgs.msg_searching()));
	            flexTable.getFlexCellFormatter().setColSpan(0, 0, 4);
				flexTable.getRowFormatter().
						setStyleName(0, "flexTable-LastRow");
				IMyApps.searchAppItemResource.getClientResource().setReference(
						API.searchAppItem(tbQuery.getText()));
				IMyApps.searchAppItemResource.search(searchObject);
			}
		}
		
		SearchHandler searchHandler = new SearchHandler();
		tbQuery.addKeyUpHandler(searchHandler);
		bnQuery.addClickHandler(searchHandler);
		
		mainPanel.add(hpQuery);
		mainPanel.add(flexTable);
	}
}
