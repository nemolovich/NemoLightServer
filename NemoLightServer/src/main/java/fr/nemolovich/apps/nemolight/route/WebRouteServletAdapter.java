package fr.nemolovich.apps.nemolight.route;

import fr.nemolovich.apps.nemolight.route.exceptions.ServerException;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import java.io.IOException;
import spark.Request;
import spark.Response;

/**
 *
 * @author Nemolovich
 */
public abstract class WebRouteServletAdapter extends WebRouteServlet {

	public WebRouteServletAdapter(String path, String context, String templateName,
		Configuration config) throws IOException {
		super(path, context, templateName, config);
	}

	@Override
	protected void doGet(Request request, Response response,
		SimpleHash root) throws ServerException{
		
	}

	@Override
	protected void doPost(Request request, Response response,
		SimpleHash root) throws ServerException{
		
	}

}
