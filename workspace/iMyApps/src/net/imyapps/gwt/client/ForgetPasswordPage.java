package net.imyapps.gwt.client;

import net.imyapps.common.Account;

import org.restlet.client.data.MediaType;
import org.restlet.client.data.Preference;
import org.restlet.client.resource.Result;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ForgetPasswordPage {

	public static void initPage(final Panels panels) {
		final TextBox loginName = new TextBox();
		final TextBox email = new TextBox();
		final Anchor question = new Anchor("(" + IMyApps.msgs.lb_question() + ")");
		final TextBox answer = new TextBox();
		
		FlexTable flexTable = new FlexTable();
		
		flexTable.setWidget(0, 0, new Label(IMyApps.msgs.lb_loginName()));
		flexTable.setWidget(0, 1, loginName);

		flexTable.setWidget(1, 0, new Label(IMyApps.msgs.lb_email()));
		flexTable.setWidget(1, 1, email);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(answer);
		hp.add(question);
		flexTable.setWidget(2, 0, new Label(IMyApps.msgs.lb_answer()));
		flexTable.setWidget(2, 1, hp);

		Button bnCancel = new Button(IMyApps.msgs.bn_cancel());
		Button bnSignup = new Button(IMyApps.msgs.bn_ok());
		HorizontalPanel hpBn = new HorizontalPanel();
		hpBn.add(bnSignup);
		hpBn.add(bnCancel);
		flexTable.setWidget(3, 0, hpBn);
		flexTable.getFlexCellFormatter().setColSpan(3, 0, 2);
		
		class SignupHandler implements ClickHandler, KeyUpHandler {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					doIt();
				}
			}

			@Override
			public void onClick(ClickEvent event) {
				doIt();
			}
			
			
			public void doIt() {
				panels.clearMessage();
				
				Account account = new Account();
				account.setLoginName(loginName.getText());
				account.setEmail(email.getText());
				account.setAnswer(answer.getText());
				
				IMyApps.signupResource.getClientResource().setReference(API.account());
				IMyApps.signupResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
						Preference<MediaType>(MediaType.APPLICATION_JSON));
				
				IMyApps.signupResource.update(account, new Result<Void>() {

					public void onFailure(Throwable caught) {
						// Handle the error
						Window.alert(caught.getMessage());
						panels.clear();
						ForgetPasswordPage.initPage(panels);
					}

					public void onSuccess(Void result) {
						Window.alert(IMyApps.msgs.msg_resend_authmail());
						panels.clear();
						LoginPage.initPage(panels);
					}
					
				});
			}
		}
		
		bnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				panels.clear();
				LoginPage.initPage(panels);
			}
		});
		
		question.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				IMyApps.signupResource.getClientResource().setReference(API.getQuestion(loginName.getText()));
				IMyApps.signupResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
						Preference<MediaType>(MediaType.APPLICATION_JSON));
				IMyApps.signupResource.retrieve(new Result<Account>() {
					@Override
					public void onSuccess(Account result) {
						Window.alert(result.getQuestion());
					}
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(IMyApps.msgs.msg_loginname_first());
					}
				});
			}
		});
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(flexTable);
		SignupHandler signupHandler = new SignupHandler();
		bnSignup.addClickHandler(signupHandler);
		loginName.addKeyUpHandler(signupHandler);
		email.addKeyUpHandler(signupHandler);
		answer.addKeyUpHandler(signupHandler);
		
		panels.getMain().add(vp);
	}
}
