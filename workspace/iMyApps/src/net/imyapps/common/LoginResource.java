package net.imyapps.common;

import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

/**
 * The resource associated to login.
 */
public interface LoginResource {
    @Post
    public Account login2(Login login);

    @Put
    public Account login();

    @Delete
    public void logout();
}
