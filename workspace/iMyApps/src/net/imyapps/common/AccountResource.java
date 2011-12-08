package net.imyapps.common;

import org.restlet.resource.Post;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * The resource associated to a account.
 */
public interface AccountResource {

    @Get
    public Account retrieve();

    @Post
    public void create(Account account);

    @Put
    public void update(Account account);

    @Delete
    public void remove();

}
