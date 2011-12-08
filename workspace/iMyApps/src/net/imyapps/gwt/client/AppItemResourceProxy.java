package net.imyapps.gwt.client;

import net.imyapps.common.AppItems;

import org.restlet.client.resource.ClientProxy;
import org.restlet.client.resource.Post;
import org.restlet.client.resource.Get;
import org.restlet.client.resource.Result;

public interface AppItemResourceProxy extends ClientProxy {
    @Get
    public void list(Result<AppItems> callback);
    @Post
    public void add(AppItems appItems, Result<String[]> callback);
}
