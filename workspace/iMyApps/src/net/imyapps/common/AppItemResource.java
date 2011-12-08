package net.imyapps.common;

import org.restlet.resource.Post;
import org.restlet.resource.Get;

/**
 * The resource associated to App Items.
 */
public interface AppItemResource {
    @Get
    public AppItems list();
    @Post
    public String[] add(AppItems appItems);
}
