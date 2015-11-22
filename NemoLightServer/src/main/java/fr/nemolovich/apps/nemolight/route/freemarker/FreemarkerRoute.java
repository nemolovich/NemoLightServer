package fr.nemolovich.apps.nemolight.route.freemarker;

import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
public interface FreemarkerRoute {

    Object doHandle(Request request, Response response);

    void enableSecurity();

}
