package fr.nemolovich.apps.nemolight.route.filter;

import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
public interface WebRouteFilter {

	public void init();

	public void destroy();

	public void doFilter(Request request, Response response);

}
