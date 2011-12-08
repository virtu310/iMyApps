package net.imyapps.gwt.client;

import net.imyapps.common.AppItems;

import org.restlet.client.resource.ClientProxy;
import org.restlet.client.resource.Get;
import org.restlet.client.resource.Result;


public interface SearchAppItemResourceProxy extends ClientProxy {
    @Get
    public void search(Result<AppItems> callback);
}
