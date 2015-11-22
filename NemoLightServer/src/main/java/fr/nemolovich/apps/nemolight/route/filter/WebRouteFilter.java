package fr.nemolovich.apps.nemolight.route.filter;

import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
public interface WebRouteFilter {

    void init();

    void destroy();

    void doFilter(Request request, Response response);

}
