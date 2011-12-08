package net.imyapps.gwt.client;

import org.restlet.client.resource.ClientProxy;
import org.restlet.client.resource.Get;
import org.restlet.client.resource.Post;
import org.restlet.client.resource.Result;

public interface ListAppResourceProxy extends ClientProxy {
	@Post
	public void postHtml(Result<String> callback);
	
    @Get
    public void getHtml(Result<String> callback);
}
