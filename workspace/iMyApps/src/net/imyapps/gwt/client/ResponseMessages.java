package net.imyapps.gwt.client;

import com.google.gwt.i18n.client.Messages;

public interface ResponseMessages extends Messages {
	String lb_loginName();
	String lb_email();
	String lb_password();
	String lb_verify();
	String lb_question();
	String lb_answer();
	String lb_metadata();
	String lb_getmore();
	String lb_itemName();
	String lb_artistName();
	String lb_copyright();
	String lb_genre();
	String lb_price();
	String lb_buyPrice();
	String lb_releaseDate();
	String lb_purchaseTime();
	String lb_buyer();
	String lb_note();
	String lb_modify();
	String lb_totalCount();
	String lb_totalPrice();
	String lb_paidCount();
	String lb_help();
	String lb_rememberLogin();
	
	String bn_login();
	String bn_register();
	String bn_forget_passwd();
	String bn_logout();
	String bn_profile();
	String bn_send();
	String bn_add_app();
	String bn_ok();
	String bn_cancel();
	String bn_close();
	String bn_previous();
	String bn_search();
	String bn_statistic();
	String bn_list();
	String bn_switchlist();
	String bn_good();
	String bn_retrieve_good();
	
	String err_name_pass_fail();
	String err_login_fail();
	String err_insert_fail();
	String err_verify_fail();
	String err_info_incorrupt();
	String err_client();
	
	String msg_help();
	String msg_successful();
	String msg_insert_successful(int size);
	String msg_signup_successful();
	String msg_searching();
	String msg_loading();
	String msg_no_found(String q);
	String msg_no_result();
	String msg_resend_authmail();
	String msg_loginname_first();
	String msg_why_question();
	
	String q_sure_to_logout();
	String q_input_note();
	String q_input_price();
	
	String pop_welcome(String loginName);
	String pop_byebye();
}
