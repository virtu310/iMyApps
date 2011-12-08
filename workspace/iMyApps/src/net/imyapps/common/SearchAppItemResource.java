package net.imyapps.common;

import org.restlet.resource.Get;

/**
 * The resource associated to Search.
 */
public interface SearchAppItemResource {
    @Get
    public AppItems search();
}
