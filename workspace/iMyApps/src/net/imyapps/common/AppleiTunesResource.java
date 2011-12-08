package net.imyapps.common;

import org.restlet.resource.Post;

/**
 * The resource associated to Apple iTunes.
 */
public interface AppleiTunesResource {
    @Post
    public String[] upload(AppleiTunes appleiTunes);
}

