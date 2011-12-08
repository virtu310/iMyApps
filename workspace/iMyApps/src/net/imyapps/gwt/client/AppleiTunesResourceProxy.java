package net.imyapps.gwt.client;

import net.imyapps.common.AppleiTunes;

import org.restlet.client.resource.ClientProxy;
import org.restlet.client.resource.Post;
import org.restlet.client.resource.Result;


public interface AppleiTunesResourceProxy extends ClientProxy {
    @Post
    public void upload(AppleiTunes appleiTunes, Result<String[]> callback);
}
