package net.imyapps.gwt.client;

import net.imyapps.common.AppleiTunes;

import org.restlet.client.data.MediaType;
import org.restlet.client.data.Preference;
import org.restlet.client.resource.Result;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ITunesPage {
	static AppleiTunesResourceProxy itunesResource = GWT.create(AppleiTunesResourceProxy.class);
	
	public static void initPage(final Panels panels) {
		Anchor anPrev = new Anchor(IMyApps.msgs.bn_previous());
		anPrev.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ListPage.switchTo(panels);
			}
		});
		panels.getTop().add(anPrev);
		
		final TextArea xml = new TextArea();
		xml.setCharacterWidth(60);
	    xml.setVisibleLines(10);
	    
		VerticalPanel vp = new VerticalPanel();
		
		VerticalPanel pXml = new VerticalPanel();
		pXml.add(new Label(IMyApps.msgs.lb_metadata()));
		pXml.add(xml);
		vp.add(pXml);
		
		Button bnSend = new Button(IMyApps.msgs.bn_send());
		
		vp.add(bnSend);
		
		class AppleiTunesHandler implements ClickHandler, KeyUpHandler {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					doSend();
				}
			}

			@Override
			public void onClick(ClickEvent event) {
				doSend();
			}
			
			
			public void doSend() {
				panels.clearMessage();
				
				AppleiTunes itunes = new AppleiTunes();
				itunes.setXml(xml.getText());
				
				itunesResource.getClientResource().setReference(API.iTunes());
				itunesResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
						Preference<MediaType>(MediaType.APPLICATION_JSON));
				
				itunesResource.upload(itunes, new Result<String[]>() {

					public void onFailure(Throwable caught) {
						// Handle the error
						Window.alert(caught.getMessage());
					}

					public void onSuccess(String result[]) {
						if (result != null) {
							panels.appendInfo(IMyApps.msgs.msg_insert_successful(result.length));
							xml.setText("");
						}
						else
							Window.alert(IMyApps.msgs.err_insert_fail());
					}
					
				});
			}
		}

		AppleiTunesHandler handler = new AppleiTunesHandler();
		bnSend.addClickHandler(handler);
		//xml.addKeyUpHandler(handler);
		
		panels.getMain().add(vp);
	}
}
