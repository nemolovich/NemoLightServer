package fr.nemolovich.apps.nemolight.route.freemarker;

import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
public interface FreemarkerRoute {

	public Object doHandle(Request request, Response response);

	public void enableSecurity();
	
}
