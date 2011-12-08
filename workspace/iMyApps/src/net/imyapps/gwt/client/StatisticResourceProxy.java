package net.imyapps.gwt.client;

import net.imyapps.common.Statistic;

import org.restlet.client.resource.ClientProxy;
import org.restlet.client.resource.Get;
import org.restlet.client.resource.Result;


public interface StatisticResourceProxy extends ClientProxy {
    @Get
    public void retrieve(Result<Statistic> callback);

}
