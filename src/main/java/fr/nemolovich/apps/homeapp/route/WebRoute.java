package fr.nemolovich.apps.homeapp.route;

import spark.Request;
import spark.Response;
import spark.Route;

public abstract class WebRoute extends Route {

	public WebRoute(String path) {
		super(path);

	}

	@Override
	public abstract Object handle(Request request, Response response);

}
