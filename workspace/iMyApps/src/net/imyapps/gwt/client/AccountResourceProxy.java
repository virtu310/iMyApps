package net.imyapps.gwt.client;

import net.imyapps.common.Account;

import org.restlet.client.resource.ClientProxy;
import org.restlet.client.resource.Delete;
import org.restlet.client.resource.Get;
import org.restlet.client.resource.Post;
import org.restlet.client.resource.Put;
import org.restlet.client.resource.Result;


public interface AccountResourceProxy extends ClientProxy {
    @Get
    public void retrieve(Result<Account> callback);

    @Post
    public void create(Account account, Result<Void> callback);

    @Put
    public void update(Account account, Result<Void> callback);

    @Delete
    public void remove(Result<Void> callback);
}
