package net.imyapps.common;

import org.restlet.resource.Get;

/**
 * The resource associated to statistic.
 */
public interface StatisticResource {

    @Get
    public Statistic getStatistic();

}
