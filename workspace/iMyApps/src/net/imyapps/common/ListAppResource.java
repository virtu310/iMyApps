package net.imyapps.common;

import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * The resource associated to App List.
 */
public interface ListAppResource {
	@Post
	public String postHtml();
	
    @Get
    public String getHtml();

}
