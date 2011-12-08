package net.imyapps.gwt.client;

import net.imyapps.common.Account;
import net.imyapps.common.Login;

import org.restlet.client.resource.ClientProxy;

import org.restlet.client.resource.Delete;
import org.restlet.client.resource.Post;
import org.restlet.client.resource.Put;
import org.restlet.client.resource.Result;


public interface LoginResourceProxy extends ClientProxy {
    @Post
    public void loginByPost(Login login, Result<Account> callback);

    @Put
    public void login(Result<Account> callback);
    
    @Delete
    public void logout(Result<Void> callback);
}
