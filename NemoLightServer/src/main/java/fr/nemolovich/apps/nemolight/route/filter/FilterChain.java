package fr.nemolovich.apps.nemolight.route.filter;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import spark.Request;
import spark.Response;

/**
 *
 * @author Administrateur
 */
public class FilterChain {

    private ConcurrentLinkedQueue<WebRouteFilter> filters;
    private Iterator<WebRouteFilter> iterator;

    public FilterChain() {
    }

    public WebRouteFilter init(
        ConcurrentLinkedQueue<Class<WebRouteFilter>> filters)
        throws InstantiationException, IllegalAccessException {
        this.filters = new ConcurrentLinkedQueue<>();
        for (Class<WebRouteFilter> cFilter : filters) {
            this.filters.add(cFilter.newInstance());
        }
        this.iterator = this.filters.iterator();
        return this.iterator.next();
    }

    public void doFilter(Request request, Response response) {
        if (this.iterator.hasNext()) {
            WebRouteFilter filter = this.iterator.next();
            filter.init();
            filter.doFilter(request, response, this);
            filter.destroy();
        }
    }
}
