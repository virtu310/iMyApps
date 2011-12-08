package net.imyapps.gwt.client;

import net.imyapps.common.Account;
import net.imyapps.gwt.shared.FieldVerifier;

import org.restlet.client.data.MediaType;
import org.restlet.client.data.Preference;
import org.restlet.client.resource.Result;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SignupPage {

	public static void initPage(final Panels panels) {
		final TextBox loginName = new TextBox();
		final TextBox email = new TextBox();
		final PasswordTextBox password = new PasswordTextBox();
		final PasswordTextBox vpassword = new PasswordTextBox();
		final TextBox question = new TextBox();
		final TextBox answer = new TextBox();
		
		FlexTable flexTable = new FlexTable();
		
		flexTable.setWidget(0, 0, new Label(IMyApps.msgs.lb_loginName()));
		flexTable.setWidget(0, 1, loginName);

		flexTable.setWidget(1, 0, new Label(IMyApps.msgs.lb_email()));
		flexTable.setWidget(1, 1, email);
		
		flexTable.setWidget(2, 0, new Label(IMyApps.msgs.lb_password()));
		flexTable.setWidget(2, 1, password);
		
		flexTable.setWidget(3, 0, new Label(IMyApps.msgs.lb_verify()));
		flexTable.setWidget(3, 1, vpassword);
		
		flexTable.setWidget(4, 0, new Label(IMyApps.msgs.lb_question()));
		flexTable.setWidget(4, 1, question);
		
		flexTable.setWidget(5, 0, new Label(IMyApps.msgs.lb_answer()));
		flexTable.setWidget(5, 1, answer);
		
		flexTable.setWidget(6, 1, new Label(IMyApps.msgs.msg_why_question()));
		//flexTable.getFlexCellFormatter().setColSpan(6, 0, 2);
		
		Button bnCancel = new Button(IMyApps.msgs.bn_cancel());
		Button bnSignup = new Button(IMyApps.msgs.bn_ok());
		HorizontalPanel pnBtn = new HorizontalPanel();
		pnBtn.add(bnSignup);
		pnBtn.add(bnCancel);
		flexTable.setWidget(7, 0, pnBtn);
		flexTable.getFlexCellFormatter().setColSpan(7, 0, 2);
		
		class SignupHandler implements ClickHandler, KeyUpHandler {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					doSignup();
				}
			}

			@Override
			public void onClick(ClickEvent event) {
				doSignup();
			}
			
			
			public void doSignup() {
				panels.clearMessage();
				
				if (password.getText().equals(vpassword.getText()) == false) {
					panels.appendError(IMyApps.msgs.err_verify_fail());
					return;
				}
				
				Account account = new Account();
				account.setLoginName(loginName.getText());
				account.setEmail(email.getText());
				account.setPassword(password.getText());
				account.setQuestion(question.getText());
				account.setAnswer(answer.getText());
				
				if (!FieldVerifier.isValidSignupAccount(account)) {
					panels.appendError(IMyApps.msgs.err_info_incorrupt());
					return;
				}
				
				IMyApps.signupResource.getClientResource().setReference(API.account());
				IMyApps.signupResource.getClientResource().getClientInfo().getAcceptedMediaTypes().add(new
						Preference<MediaType>(MediaType.APPLICATION_JSON));
				
				IMyApps.signupResource.create(account, new Result<Void>() {

					public void onFailure(Throwable caught) {
						// Handle the error
						Window.alert(caught.getMessage());
					}

					public void onSuccess(Void result) {
						Window.alert(IMyApps.msgs.msg_signup_successful());
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
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(flexTable);
		SignupHandler signupHandler = new SignupHandler();
		bnSignup.addClickHandler(signupHandler);
		loginName.addKeyUpHandler(signupHandler);
		email.addKeyUpHandler(signupHandler);
		password.addKeyUpHandler(signupHandler);
		vpassword.addKeyUpHandler(signupHandler);
		question.addKeyUpHandler(signupHandler);
		answer.addKeyUpHandler(signupHandler);
		
		panels.getMain().add(vp);
	}
}
